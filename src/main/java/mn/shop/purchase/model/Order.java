package mn.shop.purchase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "orders")
@Entity
@Getter
@Setter
public class Order implements Serializable {
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private Long id;
    private String phoneNumber;
    private String orderedProducts;
    private Double price;
    private String address;
    private String comment;
    private String email;
    private String fb;
    private OrderState orderState;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    private String transactionInfo;
    private String qpayInvoiceId;

    public Order(OrderCreateDTO dto) {
        this.phoneNumber = dto.getPhoneNumber();
        this.price = dto.getPrice();
        this.orderedProducts = dto.getOrderedProducts();
        this.address = dto.getAddress();
        this.email = dto.getEmail();
        this.fb = dto.getFb();
        this.createdAt = new Date();
    }

    public Order() {
    }

}