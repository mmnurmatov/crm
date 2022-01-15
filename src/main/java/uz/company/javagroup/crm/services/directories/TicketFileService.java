package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.TicketFile;
import uz.isd.javagroup.grandcrm.entity.directories.TicketMessage;

import java.util.List;

@Service
public interface TicketFileService {

    void saveMessageFile(TicketMessage ticketMessage, List<MultipartFile> files);

    void deleteFile(Long id);

    List<TicketFile> findAllByTicketMessageId(Long ticketMessageId);
}
