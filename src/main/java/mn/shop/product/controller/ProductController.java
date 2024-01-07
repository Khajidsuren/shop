package mn.shop.product.controller;

import lombok.extern.slf4j.Slf4j;
import mn.shop.category.service.CategoryService;
import mn.shop.product.currencyRatio.GlobalVariables;
import mn.shop.product.model.Product;
import mn.shop.product.model.ProductCreateDTO;
import mn.shop.product.model.ProductDTO;
import mn.shop.product.model.ProductDetailDTO;
import mn.shop.product.service.ProductService;
import mn.shop.product.service.S3FileUploadService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    private final S3FileUploadService fileUploadService;

    private final GlobalVariables globalVariables;

    public ProductController(ProductService productService, CategoryService categoryService, S3FileUploadService filService, GlobalVariables globalVariables) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.fileUploadService = filService;
        this.globalVariables = globalVariables;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("list")
    public ResponseEntity<List<ProductDTO>> getList() {
        return ResponseEntity.ok(productService.findAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("detail/{id:[0-9]+}")
    public ResponseEntity<ProductDetailDTO> detail(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("list/{id:[0-9]+}")
    public ResponseEntity<List<ProductDTO>> getProductList(@PathVariable("id") Long categoryId) {
        log.info("get product list categoryId: {}", categoryId);
        Set<Long> categoryIds = categoryId == 0 ? null : categoryService.getCategories(categoryId);
        return ResponseEntity.ok(productService.findByCategoryId(categoryId, categoryIds));
    }


    @CrossOrigin(origins = "*")
    @GetMapping("search")
    public ResponseEntity<Page<Product>> search(@RequestParam(name = "name", required = false) String name,
                                                @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                                @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                                @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {
        return ResponseEntity.ok(productService.search(name, categoryId, minPrice, maxPrice, page, size));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("update/{productId:[0-9]+}")
    public ResponseEntity<Long> updateProduct(@PathVariable("productId") Long productId,
                                              @RequestBody ProductCreateDTO dto) {
        log.info("start to update id : {}, dto: {}", productId, dto.toString());
        try {
            return ResponseEntity.ok(productService.update(productId, dto.getName(), dto.getPrice(), dto.getCategoryId(), dto.getIsSpecial(),
                    dto.getImg1(), dto.getImg2(), dto.getImg3(), dto.getImg4(), dto.getDescription(), dto.getInstruction(), dto.getSize(), dto.getWeight()));
        } catch (Exception e) {
            log.error("failed to update product error: {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("delete/{productId:[0-9]+}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("delete product id: {}", productId);
        return ResponseEntity.ok(productService.delete(productId));
    }


    @CrossOrigin(origins = "*")
    @PostMapping("create")
    public ResponseEntity<Long> createProduct(@RequestBody @Validated ProductCreateDTO dto) {
        log.info("start to create product name: {}", dto.getName());
        try {
            return ResponseEntity.ok(productService.create(dto));
        } catch (Exception e) {
            log.error("failed to create product error: {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("view/{id:[0-9]+}")
    public ResponseEntity<ProductDTO> view(@PathVariable("id") Long productId) {
        log.info("view product detail productId: {}", productId);
        return ResponseEntity.ok(productService.view(productId));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("id/{id:[0-9]+}")
    public ResponseEntity<Product> product(@PathVariable("id") Long productId) {
        log.info("find by id productId: {}", productId);
        return ResponseEntity.ok(productService.findById(productId));
    }

//    @CrossOrigin(origins = "*")
//    @PostMapping("upload")
//    public ResponseEntity<String> uploadImg(@RequestParam("file") MultipartFile file) {
//        try {
//            return ResponseEntity.ok(productService.saveImage(file, "test.png"));
//        } catch (Exception e) {
//            log.error("failed to create product error: {}", e.getMessage());
//            throw e;
//        }
//    }


    @CrossOrigin(origins = "*")
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        File file = new File(multipartFile);
        log.info("Start to delete file");
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();


        String filePath = fileUploadService.uploadFile(file);
        return "File uploaded successfully! Path: " + filePath;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/delete")
    public String deleteFile(@RequestParam String fileName) {
        log.info("Start to delete file : {}", fileName);
        return fileUploadService.deleteFile(fileName);
    }

}
