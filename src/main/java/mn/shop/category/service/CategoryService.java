package mn.shop.category.service;

import mn.shop.category.model.Category;
import mn.shop.category.model.CategoryDTO;
import mn.shop.category.model.CategoryNameDTO;
import mn.shop.category.repository.CategoryRepository;
import mn.shop.utils.ValidationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public List<CategoryNameDTO> getAll() {
//        List<Category> categories = repository.findAll();
//        List<CategoryNameDTO> categoryList = new ArrayList<>();
//        categories.forEach(item -> {
//            categoryList.add(getCategoryNames(item.getId()));
//        });
//        return categoryList;
        return repository.findAllCategories();
    }

    public List<Category> findByParent(Long parentId) {
        return repository.findAllByParentId(parentId);
    }

    public Category rename(Long categoryId, String newName) {
        Category category = repository.findById(categoryId).orElse(null);
        if (Objects.nonNull(category)) {
            category.setName(newName);
            repository.save(category);
        }
        return category;
    }

    public Boolean delete(Long categoryId) {
        repository.deleteById(categoryId);
        return true;
    }

    public Set<Long> getCategories(Long categoryId) {

        List<Long> patentIds = repository.findCategoryIdByParent(categoryId);
        List<Long> childIds = new ArrayList<>();
        patentIds.forEach(item -> {
            childIds.addAll(repository.findCategoryIdByParent(item));
        });

        Set<Long> categoryIds = new HashSet<>();

        if (!childIds.isEmpty()) categoryIds = Set.copyOf(childIds);
        if (categoryIds.isEmpty()) categoryIds = Set.copyOf(patentIds);
        if (categoryIds.isEmpty()) categoryIds = Set.copyOf(Collections.singleton(categoryId));

        return categoryIds;
    }

    public CategoryNameDTO getCategoryNames(Long categoryId) {
//        Category category = repository.findById(categoryId).orElse(null);
//        if(Objects.isNull(category)) throw new ValidationException("Категори олдсонгүй");
//
//        if(Objects.isNull(category.getParentId())) return new CategoryNameDTO(categoryId, category.getName());
//
//        Category childtCategory = repository.findById(category.getParentId()).orElse(null);
//        if(Objects.isNull(childtCategory)) return new CategoryNameDTO(categoryId, category.getName());
//
//        if(Objects.nonNull(childtCategory.getParentId())) {
//            Category parentCategory = repository.findById(childtCategory.getParentId()).orElse(null);
//            if(Objects.nonNull(parentCategory)) new CategoryNameDTO(categoryId, parentCategory.getName(), childtCategory.getName(), category.getName());
//        }
//
//
//        return new CategoryNameDTO(categoryId, childtCategory.getName(), category.getName());

        return repository.findCategoryName(categoryId).orElse(null);


    }

    public Category create(CategoryDTO dto) {
        return repository.save(new Category(dto.getName(), Objects.nonNull(dto.getParentId()) ? dto.getParentId() : Long.valueOf(0)));
    }

    public boolean isParent(Long categoryId) {
        Category category = repository.findById(categoryId).orElse(null);
        if (Objects.isNull(category)) throw new ValidationException("Категори олдсонгүй");
        return Objects.isNull(category.getParentId());
    }

}
