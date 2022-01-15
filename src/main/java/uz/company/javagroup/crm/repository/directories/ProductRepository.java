package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Product;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, QueryByExampleExecutor<Product> {
    List<Product> findByProductCategoryId(Long categoryId);

    @Query("SELECT p, p.productUnit.nameUz, p.productUnit.nameRu, p.productUnit.nameEn, p.productUnit.code FROM Product p WHERE LOWER(p.nameUz) LIKE %?1% OR LOWER(p.nameEn) LIKE %?1% OR LOWER(p.nameRu) LIKE %?1% OR LOWER(p.productCategory.article) LIKE %?1% ORDER BY p.nameUz,p.nameRu,p.nameEn,p.productCategory.article")
    List<Product> allByColumn(String keyword, Pageable pageable);

    @Query("SELECT p, p.productUnit.nameUz, p.productUnit.nameRu, p.productUnit.nameEn, p.productUnit.code FROM Product p ")
    List<Product> findAll(Pageable pageable);

    Product findFirstByCode(String code);
}
