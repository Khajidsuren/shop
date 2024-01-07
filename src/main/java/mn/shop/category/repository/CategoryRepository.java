package mn.shop.category.repository;

import mn.shop.category.model.Category;
import mn.shop.category.model.CategoryNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c.id from Category as c where c.parentId = :parentId")
    List<Long> findCategoryIdByParent(Long parentId);

    List<Category> findAllByParentId(Long parentId);

    @Query(value = "select new mn.shop.category.model.CategoryNameDTO(c1.id, c3.name, c2.name, c1.name) from Category as c1 left join Category as c2 on " +
                   "c1.parentId=c2.id left join Category as c3 on c2.parentId=c3.id where c1.id=:id")
    Optional<CategoryNameDTO> findCategoryName(Long id);


    @Query(value = "select new mn.shop.category.model.CategoryNameDTO(c1.id, c3.name, c2.name, c1.name) from Category as c1 left join Category as c2 on " +
                   "c1.parentId=c2.id left join Category as c3 on c2.parentId=c3.id")
    List<CategoryNameDTO> findAllCategories();

}
