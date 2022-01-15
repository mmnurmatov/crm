package uz.isd.javagroup.grandcrm.Impl.modules;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.UserFiles;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.UserFileRepository;
import uz.isd.javagroup.grandcrm.repository.modules.UserRepository;
import uz.isd.javagroup.grandcrm.services.directories.UploadPathService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.utility.SecurityUtility;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UploadPathService uploadPathService;

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private ServletContext context;

    private BCryptPasswordEncoder passwordEncoder() {
        return SecurityUtility.passwordEncoder();
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        User localUser = userRepository.findByUsername(user.getUsername());
        if (localUser != null)
            LOG.info("user {} already exists. Nothing will be done.", user.getUsername());
        else localUser = userRepository.save(user);
        return localUser;
    }

    @Override
    public UserFiles findFileByUserId(Long userId) {
        return userFileRepository.findByUserId(userId);
    }

    @Override
    public void save(User user) {
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws RecordNotFoundException {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public void updateUser(User user, MultipartFile file) throws IOException {
        if (!file.isEmpty()){
            File pathFile = new File("./user-photos/" + user.getId() + "/" + File.separator + user.getImageUrl());
            if (pathFile.exists()) {
                pathFile.delete();
            }

            user.setUpdatedAt(new Date());
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            user.setImageUrl(fileName);

            String uploadDir = "./user-photos/" + user.getId();

            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()){
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                throw new IOException("Could not save uploaded file: " + fileName);
            }
        }

        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> byCompanyId(Long companyId) {
        return userRepository.findByCompanyId(companyId);
    }

    @Override
    public List<User> byRoleId(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

    @Override
    public User getByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public List<User> getUsersByCompanyIdAndRoleId(Long companyId, Long roleId) {
        return userRepository.getUsersByCompanyIdAndRoleId(companyId, roleId);
    }
}
