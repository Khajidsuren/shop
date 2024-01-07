package mn.shop.product.currencyRatio;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class GlobalVariables {
    private BigDecimal ratio;

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio.setScale(3, RoundingMode.HALF_UP);
    }

}

