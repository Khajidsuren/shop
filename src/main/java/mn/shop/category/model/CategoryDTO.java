package mn.shop.category.model;


import lombok.Getter;

@Getter
public class CategoryDTO {

    private String name;
    private Long parentId;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

}
