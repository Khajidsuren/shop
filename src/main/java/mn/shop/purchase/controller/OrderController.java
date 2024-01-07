package mn.shop.purchase.controller;


import lombok.extern.slf4j.Slf4j;
import mn.shop.purchase.model.*;
import mn.shop.purchase.service.ExcelWriter;
import mn.shop.purchase.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateDTO dto) {
        log.info("start to create order dto: {}", dto.toString());
        try {
            return ResponseEntity.ok(orderService.createOrder(dto));
        } catch (Exception e) {
            log.error("failed to create order error: {}", e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("update-state/{id:[0-9]+}")
    public ResponseEntity<Order> updateOrderState(@PathVariable Long id, @RequestParam OrderState state) {
        log.info("start to update {} order state into {}", id, state);
        try {
            return ResponseEntity.ok(orderService.updateState(id, state));
        } catch (Exception e) {
            log.error("failed to update order state error: {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("check-payment/{id:[0-9]+}")
    public ResponseEntity<Order> checkPayment(@PathVariable Long id) {
        log.info("start to check order is paid {}", id);
        try {
            return ResponseEntity.ok(orderService.checkIsPaid(id));
        } catch (Exception e) {
            log.error("failed to check order is paid: {}", e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("delete/{id:[0-9]+}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long id) {
        log.info("start to delete order {}", id);
        try {
            return ResponseEntity.ok(orderService.delete(id));
        } catch (Exception e) {
            log.error("failed to update order state error: {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    public ResponseEntity<Page<OrderView>> getOrderList(@RequestParam(name = "phoneNumber", required = false) String phone,
                                                        @RequestParam(name = "address", required = false) String address,
                                                        @RequestParam(name = "orderedProduct", required = false) String product,
                                                        @RequestParam(name = "state", required = false) OrderState state,
                                                        @RequestParam(name = "info", required = false) String info,
                                                        @RequestParam(name = "minPrice", required = false) Double minPrice,
                                                        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                        @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date from,
                                                        @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date to,
                                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {
        log.info("start to get order list");
        try {
            return ResponseEntity.ok(orderService.search(phone, address, product, state, info, minPrice, maxPrice, from, to, page, size));
        } catch (Exception e) {
            log.error("failed to get order list error: {}", e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("download")
    public ResponseEntity<byte[]> download(@RequestParam(name = "phoneNumber", required = false) String phone,
                                                        @RequestParam(name = "address", required = false) String address,
                                                        @RequestParam(name = "orderedProduct", required = false) String product,
                                                        @RequestParam(name = "state", required = false) OrderState state,
                                                        @RequestParam(name = "info", required = false) String info,
                                                        @RequestParam(name = "minPrice", required = false) Double minPrice,
                                                        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                        @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date from,
                                                        @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date to) {
        log.info("start to get download list");
        try {
            List<OrderView> orders = orderService.search(phone, address, product, state, info, minPrice, maxPrice, from, to);
            byte[] excelContent = ExcelWriter.writeToExcel(orders);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "orders.xlsx");

            return new ResponseEntity<>(excelContent, headers, 200);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("qpay-invoice")
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody OrderCreateDTO orderCreateDTO) {
        log.info("crete invoice orderDto: {}", orderCreateDTO.toString());
        try {
            return ResponseEntity.ok(orderService.createInvoice(orderCreateDTO));
        } catch (Exception e) {
            log.error("failed to create invoice error: {}", e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("qpay-token")
    public ResponseEntity<String> token() {
        log.info("Get qpay access token");
        try {
            return ResponseEntity.ok(orderService.getAccessToken());
        } catch (Exception e) {
            log.error("failed to get qpay access token error: {}", e.getMessage());
            throw e;
        }
    }

}
