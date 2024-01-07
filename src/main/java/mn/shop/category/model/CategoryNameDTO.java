package mn.shop.category.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Setter
@NoArgsConstructor
public class CategoryNameDTO {

    private Long id;
    private String parent;
    private String child;
    private String category;

    public CategoryNameDTO(Long id, String parent, String child, String category) {
        this.id = id;
        if (!StringUtils.hasText(parent)) {
            if (StringUtils.hasText(child)) {
                parent = child;
                child = category;
            } else parent = category;
            category = null;
        }
        this.parent = parent;
        this.child = child;
        this.category = category;
    }


    public CategoryNameDTO(Long id, String parent) {
        this.id = id;
        this.parent = parent;
    }

    public CategoryNameDTO(Long id, String parent, String child) {
        this.id = id;
        this.parent = parent;
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getChild() {
        return child;
    }

    public String getCategory() {
        return category;
    }
}
