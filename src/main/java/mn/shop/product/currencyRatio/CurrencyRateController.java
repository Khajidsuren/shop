package mn.shop.product.currencyRatio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("converter")
@Slf4j
public class CurrencyRateController {

    private final CurrencyRationService currencyRationService;

    public CurrencyRateController(CurrencyRationService currencyRationService) {
        this.currencyRationService = currencyRationService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("update")
    public ResponseEntity<BigDecimal> updateRatio() {
        log.info("start to get currency ratio");
        return ResponseEntity.ok(currencyRationService.updateRatio());
    }


    @CrossOrigin(origins = "*")
    @GetMapping("get")
    public ResponseEntity<BigDecimal> get() {
        log.info("start to get currency ratio");
        return ResponseEntity.ok(currencyRationService.getRatio());
    }

}
