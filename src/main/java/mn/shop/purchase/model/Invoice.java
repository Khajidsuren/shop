//package mn.shop.purchase.model;
//
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import mn.shop.category.model.Category;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Date;
//
//@Table(name = "invoice")
//@Entity
//@Data
//@Deprecated
//public class Invoice implements Serializable {
//    @SequenceGenerator(
//            name = "invoice_sequence",
//            sequenceName = "invoice_id",
//            allocationSize = 1
//    )
//    @Id
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "invoice_sequence"
//    )
//    private Long id;
//
//    private Double amount;
//
//    private String phone;
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "created_at", nullable = false)
//    private Date createdAt;
//
//    @Column(name = "order_id", nullable = false, updatable = false)
//    private Long orderId;
//
//    private Long qPayInvoiceId;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
//    private Order order;
//
//    private Boolean isPaid;
//
//}