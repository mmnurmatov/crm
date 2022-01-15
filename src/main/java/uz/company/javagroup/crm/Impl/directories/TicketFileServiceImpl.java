package uz.isd.javagroup.grandcrm.Impl.directories;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.TicketFile;
import uz.isd.javagroup.grandcrm.entity.directories.TicketMessage;
import uz.isd.javagroup.grandcrm.repository.directories.TicketFileRepository;
import uz.isd.javagroup.grandcrm.services.directories.TicketFileService;
import uz.isd.javagroup.grandcrm.services.directories.UploadPathService;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class TicketFileServiceImpl implements TicketFileService {

    @Autowired
    private TicketFileRepository ticketFileRepository;
    @Autowired
    private UploadPathService uploadPathService;
    @Autowired
    private ServletContext context;

    @Override
    public void saveMessageFile(TicketMessage ticketMessage, List<MultipartFile> files) {
        if (files != null && files.size() > 0)
            for (MultipartFile file : files) {
                if (StringUtils.hasText(file.getOriginalFilename())) {
                    String fileName = file.getOriginalFilename();
                    String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(fileName);
                    File storeFile = uploadPathService.getFilePath(modifiedFileName, "images/ticketFiles");
                    if (storeFile != null) {
                        try {
                            FileUtils.writeByteArrayToFile(storeFile, file.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    TicketFile ticketFile = new TicketFile();
                    ticketFile.setTicketMessage(ticketMessage);
                    ticketFile.setFileName(fileName);
                    ticketFile.setModifiedFileName(modifiedFileName);
                    ticketFile.setFileExtension(FilenameUtils.getExtension(fileName));
                    ticketFileRepository.save(ticketFile);
                }
            }
    }

    @Override
    public List<TicketFile> findAllByTicketMessageId(Long ticketMessageId) {
        return ticketFileRepository.findAllByTicketMessageId(ticketMessageId);
    }

    @Override
    public void deleteFile(Long id) {
        Optional<TicketFile> ticketFile = ticketFileRepository.findById(id);
        if (ticketFile.isPresent()) {
            if (ticketFile.get().getModifiedFileName() != null) {
                File file = new File(context.getRealPath("/images/product/" + File.separator + ticketFile.get().getModifiedFileName()));
                if (file.exists()) {
                    file.delete();
                }
            }
            ticketFileRepository.delete(ticketFile.get());
        }
    }
}
