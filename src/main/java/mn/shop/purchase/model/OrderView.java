package mn.shop.purchase.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class OrderView implements Serializable {

    private Long id;
    private String phoneNumber;
    private String orderedProducts;
    private Double price;
    private String address;
    private String comment;
    private String email;
    private String fb;
    private OrderState orderState;
    private String createdAt;
    private String transactionInfo;

    public OrderView(Long id, String phoneNumber, String orderedProducts, Double price, String address, String comment, String email,
                     String fb, OrderState orderState, Date createdAt, String transactionInfo) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.orderedProducts = orderedProducts;
        this.price = price;
        this.address = address;
        this.comment = comment;
        this.email = email;
        this.fb = fb;
        this.orderState = orderState;
        this.createdAt = getFormattedCreatedAt(createdAt);
        this.transactionInfo = transactionInfo;
    }

    public String getFormattedCreatedAt(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return dateFormat.format(date);
    }

}
