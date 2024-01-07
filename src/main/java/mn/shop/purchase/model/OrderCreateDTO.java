package mn.shop.purchase.model;


import lombok.Data;

@Data
public class OrderCreateDTO {

    private String phoneNumber;
    private String orderedProducts;
    private Double price;
    private String address;
    private String fb;
    private String comment;
    private String email;

}
