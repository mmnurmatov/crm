package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */

@Service
public interface ProductService {

    List<Product> findAll();

    List<Product> findAll(Product product);

    void saveProduct(Product product);

    Product getProductById(Long id) throws RecordNotFoundException;

    void deleteProduct(Long id);

    void forSaveProductPhoto(Product product, MultipartFile productPhoto) throws IOException;

    void forUpdateProductPhoto(Product product, MultipartFile productPhoto) throws IOException;

    List<Product> findByCategory(Long categoryId);

    List<Product> allByColumn(String keyword, int i);

    List<Product> all(int i);

    Product findFirstByCode(String code);
}
