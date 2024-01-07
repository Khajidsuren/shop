package mn.shop.category.controller;

import lombok.extern.slf4j.Slf4j;
import mn.shop.category.model.Category;
import mn.shop.category.model.CategoryDTO;
import mn.shop.category.model.CategoryNameDTO;
import mn.shop.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @CrossOrigin(origins = "*")
    @GetMapping("list")
    public ResponseEntity<List<Category>> getCategoryList() {
        log.info("get category list");
        return ResponseEntity.ok(categoryService.findAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("list/{parentId:[0-9]+}")
    public ResponseEntity<List<Category>> getCategoryList(@PathVariable("parentId") Long parentId) {
        log.info("get category list by parent parentId: {}", parentId);
        return ResponseEntity.ok(categoryService.findByParent(parentId));
    }


    @CrossOrigin(origins = "*")
    @GetMapping("list-names")
    public ResponseEntity<List<CategoryNameDTO>> getCategories() {
        log.info("get category name list");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("delete/{categoryId:[0-9]+}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        log.info("delete category by id categoryId: {}", categoryId);
        return ResponseEntity.ok(categoryService.delete(categoryId));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO dto) {
        log.info("create category name: {}", dto.getName());
        log.info("create category name: {}", dto.getName());
        return ResponseEntity.ok(categoryService.create(dto));
    }

}
