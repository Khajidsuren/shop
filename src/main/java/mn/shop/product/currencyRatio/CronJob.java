package mn.shop.product.currencyRatio;

import lombok.extern.slf4j.Slf4j;
import mn.shop.purchase.service.OrderService;
import mn.shop.utils.ValidationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CronJob {
    private final CurrencyRationService currencyRationService;
    private final OrderService orderService;

    public CronJob(CurrencyRationService currencyRationService, OrderService orderService) {
        this.currencyRationService = currencyRationService;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 86400000)
    public void updateCurrencyRatio() {

        try {
            currencyRationService.updateRatio();
        } catch (Exception e) {
            log.error("failed to get currency converter ratio");
            throw new ValidationException("Мөнгөн тэмдэгт хөрвүүлэхэд алдаа гарлаа.");
        }

    }

    @Scheduled(fixedRate = 3600000)
    public void updateOrderState() {
        try {
            orderService.updateOrderState();
        } catch (Exception e) {
            log.error("failed to update order state");
            throw new ValidationException("Захиалгын төлөв өөрчилөхөд алдаа гарлаа.");
        }

    }

}