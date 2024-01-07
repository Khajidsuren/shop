package mn.shop.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mn.shop.category.model.CategoryNameDTO;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal usdPrice;
    private String parentCategory;
    private String childCategory;
    private String category;
    private Long categoryId;
    private String img;
    private Boolean isSpecial;
    private Boolean isNew;

    public ProductDTO(Long id, String name, BigDecimal price,
                      String parentCategory, String childCategory, String category, Long categoryId,
                      String img1, BigDecimal usdPrice, Boolean isSpecial, Boolean isNew) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
        this.category = category;
        this.categoryId = categoryId;
        this.img = img1;
        this.usdPrice = usdPrice;
        this.isSpecial = isSpecial;
        this.isNew = isNew;
    }

    public static ProductDTO mapper(Product product, CategoryNameDTO categoryNameDTO, BigDecimal ratio) {

        if (Objects.isNull(product))
            return null;

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                StringUtils.hasText(categoryNameDTO.getParent()) ? categoryNameDTO.getParent() : null,
                StringUtils.hasText(categoryNameDTO.getChild()) ? categoryNameDTO.getChild() : null,
                StringUtils.hasText(categoryNameDTO.getCategory()) ? categoryNameDTO.getCategory() : null,
                product.getCategoryId(),
                product.getImg1(),
                product.getPrice().multiply(ratio),
                product.getIsSpecial(),
                product.getIsNew()
        );

    }

}
