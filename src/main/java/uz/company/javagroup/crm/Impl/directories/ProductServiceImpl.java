package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ProductRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.directories.UploadPathService;

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

/**
 * Created by hp-pc on 5/12/2020.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UploadPathService uploadPathService;
    @Autowired
    private ServletContext context;

    @Override
    public List<Product> findAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products;
    }

    @Override
    public List<Product> findAll(Product product) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Product> example = Example.of(product, matcher);
        return (List<Product>) productRepository.findAll(example);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) throws RecordNotFoundException {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) return productOpt.get();
        else throw new RecordNotFoundException("No Product record exists for id: " + id);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void forSaveProductPhoto(Product product, MultipartFile productPhoto) throws IOException {
        if (!productPhoto.isEmpty()) {

            String fileName = StringUtils.cleanPath(productPhoto.getOriginalFilename());
            product.setModifiedPhotoName(fileName);

            String uploadDir = "./product-photos/";

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = productPhoto.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save uploaded file: " + fileName);
            }
        }

        productRepository.save(product);
    }

    @Override
    public void forUpdateProductPhoto(Product product, MultipartFile productPhoto) throws IOException {
        if (!productPhoto.isEmpty()) {
            File pathFile = new File("./product-photos/" + File.separator + product.getModifiedPhotoName());
            if (pathFile.exists()) {
                pathFile.delete();
            }

            product.setUpdatedAt(new Date());
            String fileName = StringUtils.cleanPath(productPhoto.getOriginalFilename());
            product.setModifiedPhotoName(fileName);

            String uploadDir = "./product-photos/";

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = productPhoto.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save uploaded file: " + fileName);
            }
        }

        productRepository.save(product);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByProductCategoryId(categoryId);
    }

    @Override
    public List<Product> allByColumn(String keyword, int i) {
        Pageable pageable = PageRequest.of(0, i);
        return productRepository.allByColumn(keyword, pageable);
    }

    @Override
    public List<Product> all(int i) {
        Pageable pageable = PageRequest.of(0, i);
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findFirstByCode(String code) {
        return productRepository.findFirstByCode(code);
    }
}
