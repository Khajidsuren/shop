package mn.shop.category.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "category")
public class Category {
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name", nullable = false)
    private String name;

    public Category(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Category() {
    }

}
