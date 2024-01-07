package mn.shop.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mn.shop.category.model.CategoryNameDTO;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class ProductDetailDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String instruction;
    private String size;
    private String weight;
    private String material;
    private BigDecimal usdPrice;
    private String parentCategory;
    private String childCategory;
    private String category;
    private Long categoryId;
    private List<String> image;
    private Boolean isSpecial;
    private Boolean isNew;

    public ProductDetailDTO(Long id, String name, BigDecimal price, String description, String instruction, String size, String weight, String material,
                            String parentCategory, String childCategory, String category, Long categoryId,
                            String img1, String img2, String img3, String img4, BigDecimal usdPrice, Boolean isSpecial) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.instruction = instruction;
        this.size = size;
        this.weight = weight;
        this.material = material;
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
        this.category = category;
        this.categoryId = categoryId;
        this.image = new ArrayList<>(Arrays.asList(img1, img2, img3, img4));
        this.usdPrice = usdPrice;
        this.isSpecial = isSpecial;
    }

    public static ProductDetailDTO mapper(Product product, CategoryNameDTO categoryNameDTO, BigDecimal ratio) {

        if (Objects.isNull(product))
            return null;

        return new ProductDetailDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getInstruction(),
                product.getSize(),
                product.getWeight(),
                product.getMaterial(),
                StringUtils.hasText(categoryNameDTO.getParent()) ? categoryNameDTO.getParent() : null,
                StringUtils.hasText(categoryNameDTO.getChild()) ? categoryNameDTO.getChild() : null,
                StringUtils.hasText(categoryNameDTO.getCategory()) ? categoryNameDTO.getCategory() : null,
                product.getCategoryId(),
                product.getImg1(),
                product.getImg2(),
                product.getImg3(),
                product.getImg4(),
                product.getPrice().divide(ratio, RoundingMode.HALF_UP),
                product.getIsSpecial()
        );

    }

}
