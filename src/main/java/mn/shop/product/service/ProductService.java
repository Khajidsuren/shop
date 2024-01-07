package mn.shop.product.service;

import lombok.extern.slf4j.Slf4j;
import mn.shop.category.model.CategoryNameDTO;
import mn.shop.category.service.CategoryService;
import mn.shop.product.currencyRatio.GlobalVariables;
import mn.shop.product.model.Product;
import mn.shop.product.model.ProductCreateDTO;
import mn.shop.product.model.ProductDTO;
import mn.shop.product.model.ProductDetailDTO;
import mn.shop.product.repository.ProductRepository;
import mn.shop.utils.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final S3FileUploadService fileUploadService;
    private final CategoryService categoryService;
    private final GlobalVariables globalVariables;

    public ProductService(ProductRepository productRepository,
                          S3FileUploadService fileUploadService,
                          CategoryService categoryService,
                          GlobalVariables globalVariables) {
        this.productRepository = productRepository;
        this.fileUploadService = fileUploadService;
        this.categoryService = categoryService;
        this.globalVariables = globalVariables;
    }

    private List<ProductDTO> getList(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(item -> {
            CategoryNameDTO categoryNameDTO = categoryService.getCategoryNames(item.getCategoryId());
            productDTOS.add(ProductDTO.mapper(item, categoryNameDTO, globalVariables.getRatio()));
        });
        return productDTOS;
    }

    public ProductDetailDTO getProductDetail(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (Objects.isNull(product)) throw new ValidationException("Бүтээгдэхүүн олдсонгүй");
        CategoryNameDTO categoryNameDTO = categoryService.getCategoryNames(product.getCategoryId());
        return ProductDetailDTO.mapper(product, categoryNameDTO, globalVariables.getRatio());
    }

    public List<ProductDTO> findAll() {
        return getList(productRepository.findAll());
    }

    public Product findById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (Objects.isNull(product)) throw new ValidationException("Бүтээгдэхүүн олдсонгүй");
        return product;
    }

    public List<ProductDTO> findByCategoryId(Long categoryId, Set<Long> categories) {
        if (categoryId == 0) return findAll();
        return getList(productRepository.findAllByCategoryId(categories));
    }

    public Page<Product> search(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer size) {
        if (StringUtils.hasText(name)) name = name.toLowerCase();
        return productRepository.search(name, categoryId, minPrice, maxPrice, PageRequest.of(page, size));
    }

    public ProductDTO view(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return ProductDTO.mapper(product, categoryService.getCategoryNames(product.getCategoryId()), globalVariables.getRatio());
    }

    public void checkSpecial() {
        List<Product> specialProduct = productRepository.findAllByIsSpecialOrderByCreatedAtAsc(true);
        if (specialProduct.size() >= 3) {
            Product p = specialProduct.get(0);
            p.setIsSpecial(false);
            productRepository.save(p);
        }
    }

    public void checkNew() {
        List<Product> specialProduct = productRepository.findAllByIsSpecialOrderByCreatedAtAsc(true);
        if (specialProduct.size() >= 3) {
            Product p = specialProduct.get(0);
            p.setIsSpecial(false);
            productRepository.save(p);
        }
    }

    public Long update(Long id, String name, BigDecimal price, Long categoryId, Boolean isSpecial, String img1, String img2, String img3, String img4,
                       String description, String instruction, String size, String weight) {
        Product product = productRepository.findById(id).orElse(null);
        if (Objects.nonNull(product)) {
            if (price != null && !product.getPrice().equals(price)) product.setPrice(price);
            if (StringUtils.hasText(name) && !product.getName().equals(name)) product.setName(name);
            if (StringUtils.hasText(description) & product.getDescription().equals(description))
                product.setDescription(description);
//                fileUploadService.deleteFile(product.getImgUrl().substring(47));
            if (StringUtils.hasText(img1) && !product.getImg1().equals(img1)) product.setImg1(img1);
            if (StringUtils.hasText(img2) && !product.getImg2().equals(img2)) product.setImg2(img2);
            if (StringUtils.hasText(img3) && !product.getImg3().equals(img3)) product.setImg3(img3);
            if (StringUtils.hasText(img4) && !product.getImg4().equals(img4)) product.setImg4(img4);
            if (Objects.nonNull(categoryId) && !Objects.equals(product.getCategoryId(), categoryId))
                product.setCategoryId(categoryId);
            if (StringUtils.hasText(instruction) && !product.getInstruction().equals(instruction))
                product.setInstruction(instruction);
            if (StringUtils.hasText(size) && !product.getSize().equals(size)) product.setSize(size);
            if (StringUtils.hasText(weight) && !product.getWeight().equals(weight)) product.setWeight(weight);
            if (isSpecial) checkSpecial();
            product.setIsSpecial(isSpecial);
            product.setUpdatedAt(new Date());
            return productRepository.save(product).getId();
        }
        return null;
    }

    public Boolean delete(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (Objects.nonNull(product)) {
//            fileUploadService.deleteFile(product.getImgUrl().substring(47));
            productRepository.delete(product);
        } else {
            throw new ValidationException("Бүтээгдэхүүн олдсонгүй");
        }
        return true;
    }

    public Long create(ProductCreateDTO dto) {
        Product product = new Product(dto);
        if (dto.getIsSpecial()) {
            checkSpecial();
        }
        return productRepository.save(product).getId();
    }

    public String uploadS3(MultipartFile multipartFile) {
        try {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(multipartFile.getBytes());
            outputStream.close();
            return fileUploadService.uploadFile(file);
        } catch (Exception e) {
            log.info("failed to save image s3");
            throw new ValidationException("Зураг хадгалахад алдаа гарлаа");
        }

    }

//    public String saveImage(MultipartFile img, String name) {
//        String directory = "productImage/";
//
//        try {
//            File file = new File(directory + name);
//            FileOutputStream fosFor = new FileOutputStream(file);
//            fosFor.write(img.getBytes());
//            fosFor.close();
//            return file.getAbsolutePath();
//
//
//        } catch (Exception e) {
//            throw new ValidationException("failed to save img");
//        }
//    }
//
//    public String convertBase64(MultipartFile file) {
//        try {
//            byte[] fileContent = file.getBytes();
//            return Base64.getEncoder().encodeToString(fileContent);
//        } catch (Exception e) {
//            log.error("failed to convert img into base 64");
//            throw new ValidationException("Зураг хөрвүүлэхэд алдаа гарлаа");
//        }
//    }

}
