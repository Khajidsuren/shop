package mn.shop.purchase.repository;

import mn.shop.purchase.model.Order;
import mn.shop.purchase.model.OrderState;
import mn.shop.purchase.model.OrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select new mn.shop.purchase.model.OrderView(o.id, o.phoneNumber, o.orderedProducts, o.price, o.address, o.comment, " +
           "o.email, o.fb, o.orderState, o.createdAt, o.transactionInfo) from Order as o where " +
           "(:phone is null or lower(o.phoneNumber) like %:phone%) and " +
           "(:address is null or lower(o.address) like %:address%) and " +
           "(:product is null or lower(o.orderedProducts) like %:product%) and " +
           "(:info is null or lower(o.transactionInfo) like %:info%) and " +
           "(:state is null or o.orderState = :state) and " +
           "(:minPrice is null or o.price >= :minPrice) and " +
           "(:maxPrice is null or o.price <= :maxPrice) and " +
           "(CAST(:from as date) is null or :from <= o.createdAt) and " +
           "(CAST(:to as date) is null or :to >= o.createdAt)")
    Page<OrderView> search(String phone,
                           String address,
                           String product,
                           OrderState state,
                           String info,
                           Double minPrice,
                           Double maxPrice,
                           Date from,
                           Date to,
                           Pageable pageable);


    @Query("select new mn.shop.purchase.model.OrderView(o.id, o.phoneNumber, o.orderedProducts, o.price, o.address, o.comment, " +
           "o.email, o.fb, o.orderState, o.createdAt, o.transactionInfo) from Order as o where " +
           "(:phone is null or lower(o.phoneNumber) like %:phone%) and " +
           "(:address is null or lower(o.address) like %:address%) and " +
           "(:product is null or lower(o.orderedProducts) like %:product%) and " +
           "(:info is null or lower(o.transactionInfo) like %:info%) and " +
           "(:state is null or o.orderState = :state) and " +
           "(:minPrice is null or o.price >= :minPrice) and " +
           "(:maxPrice is null or o.price <= :maxPrice) and " +
           "(CAST(:from as date) is null or :from <= o.createdAt) and " +
           "(CAST(:to as date) is null or :to >= o.createdAt)")
    List<OrderView> search(String phone,
                           String address,
                           String product,
                           OrderState state,
                           String info,
                           Double minPrice,
                           Double maxPrice,
                           Date from,
                           Date to);
    @Query("select new mn.shop.purchase.model.OrderView(o.id, o.phoneNumber, o.orderedProducts, o.price, o.address, o.comment, " +
           "o.email, o.fb, o.orderState, o.createdAt, o.transactionInfo) from Order as o")
    Page<OrderView> getList(Pageable pageable);


    @Query("select o from Order as o where o.qpayInvoiceId = :invoiceId")
    Optional<Order> findByQpayInvoiceId(String invoiceId);


    @Query("select o from Order as o where o.orderState = :orderState and o.createdAt >= :beginDate")
    List<Order> findByStateAndCreatedAt(OrderState orderState, Date beginDate);
}
