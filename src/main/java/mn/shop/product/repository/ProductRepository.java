package mn.shop.product.repository;

import mn.shop.product.model.Product;
import mn.shop.product.model.ProductInt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product as p where (:name is null or lower(p.name) like %:name%) and " +
           "(:categoryId is null or p.categoryId = :categoryId) and" +
           "(:minPrice is null or (p.price >= :minPrice)) and " +
           "(:maxPrice is null or (p.price <= :maxPrice))")
    Page<Product> search(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);


    @Query("select p.id as id, c.name as category, p.name as name, p.price as price, p.img1 as img1 from Category as c inner join Product as p on p.categoryId = c.id")
    List<ProductInt> findList();

    @Query("select p.id as id, c.name as category, p.name as name, p.price as price, p.img1 as img1 " +
           "from Category as c inner join Product as p on p.categoryId = c.id where p.categoryId = :categoryId")
    List<ProductInt> findByCategoryId(Long categoryId);


    @Query("select p from Product as p where p.categoryId in :categoryId")
    List<Product> findAllByCategoryId(Set<Long> categoryId);

    @Query("select p.id as id, c.name as category, p.name as name, p.price as price, p.img1 as img1 " +
           "from Category as c inner join Product as p on p.categoryId = c.id where p.categoryId in :categoryIds")
    List<ProductInt> findByCategoryId(Set<Long> categoryIds);

    @Query("select p.id as id, c.name as category, p.name as name, p.price as price, p.img1 as img1 " +
           "from Category as c, Product as p where p.categoryId in :categoryList and c.id = p.categoryId")
    List<ProductInt> findByParentCategoryId(Set<Long> categoryList);

    @Query("select p.id as id, c.name as category, p.name as name, p.price as price, p.img1 as img1 " +
           "from Category as c, Product as p where p.categoryId in (select distinct id from Category as c where c.parentId = :id) and c.id = p.categoryId")
    List<ProductInt> findByParentCategoryId(Long id);


    List<Product> findAllByIsSpecialOrderByCreatedAtAsc(Boolean isSpecial);

}
