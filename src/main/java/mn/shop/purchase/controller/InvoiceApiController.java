package mn.shop.purchase.controller;

import lombok.extern.slf4j.Slf4j;
import mn.shop.purchase.service.OrderService;
import mn.shop.utils.ValidationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceApiController {
    private final OrderService orderService;

    public InvoiceApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 3600000)
    public void updateOrderState() {

        try {
            orderService.updateOrderState();
        } catch (Exception e) {
            log.error("failed to get currency converter ratio");
            throw new ValidationException("Мөнгөн тэмдэгт хөрвүүлэхэд алдаа гарлаа.");
        }

    }

}