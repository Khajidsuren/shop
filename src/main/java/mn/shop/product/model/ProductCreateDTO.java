package mn.shop.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductCreateDTO {

    private String name;
    private BigDecimal price;
    private Long categoryId;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String description;
    private String size;
    private String instruction;
    private String weight;
    private String material;
    private Boolean isSpecial;

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
               "name='" + name + '\'' +
               ", price=" + price +
               ", categoryId=" + categoryId +
               ", img1='" + img1 + '\'' +
               ", img2='" + img2 + '\'' +
               ", img3='" + img3 + '\'' +
               ", img4='" + img4 + '\'' +
               ", description='" + description + '\'' +
               ", size='" + size + '\'' +
               ", instruction='" + instruction + '\'' +
               ", weight='" + weight + '\'' +
               ", material='" + material + '\'' +
               ", isSpecial=" + isSpecial +
               '}';
    }
}
