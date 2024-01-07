package mn.shop.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mn.shop.category.model.Category;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category_id", nullable = false, updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "img1", nullable = false)
    private String img1;

    @Column(name = "img2", nullable = false)
    private String img2;

    @Column(name = "img3", nullable = false)
    private String img3;

    @Column(name = "img4", nullable = false)
    private String img4;

    @Column(name = "description")
    private String description;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "size")
    private String size;

    @Column(name = "weight")
    private String weight;

    @Column(name = "material")
    private String material;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "is_special")
    @ColumnDefault("false")
    private Boolean isSpecial;

    @Column(name = "is_new")
    @ColumnDefault("true")
    private Boolean isNew;

//    @Column(name = "created_by")
//    private String createdBy;
//
//    @Column(name = "updated_by")
//    private String updatedBy;

    public Product(String name, Long categoryId, BigDecimal price, String imgUrl1, String imgUrl2, String imgUrl3, String imgUrl4,
                   String description, String instruction, String size, String weight, String material, Boolean isSpecial, Boolean isNew) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.description = description;
        this.instruction = instruction;
        this.size = size;
        this.weight = weight;
        this.material = material;
        this.img1 = imgUrl1;
        this.img2 = imgUrl2;
        this.img3 = imgUrl3;
        this.img4 = imgUrl4;
        this.isSpecial = isSpecial;
        this.isNew = isNew;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Product(ProductCreateDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.categoryId = dto.getCategoryId();
        this.description = dto.getDescription();
        this.instruction = dto.getInstruction();
        this.size = dto.getSize();
        this.weight = dto.getWeight();
        this.material = dto.getMaterial();
        this.img1 = dto.getImg1();
        this.img2 = dto.getImg2();
        this.img3 = dto.getImg3();
        this.img4 = dto.getImg4();
        this.isSpecial = dto.getIsSpecial();
        this.isNew = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PrePersist
    public void onPrePersist() {
        if (this.isSpecial == null)
            this.isSpecial = false;
    }

}
