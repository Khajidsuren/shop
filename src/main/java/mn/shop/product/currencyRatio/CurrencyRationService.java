package mn.shop.product.currencyRatio;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mn.shop.utils.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CurrencyRationService {
    private final GlobalVariables globalVariables;
    private final String apiKey;

    public CurrencyRationService(@Value("${currency-convert.api-key}") String apiKey,
                                 GlobalVariables globalVariables) {
        this.apiKey = apiKey;
        this.globalVariables = globalVariables;
    }

    @PostConstruct
    public void setRatio() {
        if (Objects.isNull(globalVariables.getRatio())) updateRatio();
    }

    public BigDecimal getRatio() {
        return globalVariables.getRatio();
    }

    public BigDecimal updateRatio() {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url1 = "http://api.exchangeratesapi.io/v1/latest?access_key=" + apiKey + "&base=EUR&symbols=USD,MNT";

            ResponseEntity<String> response1 = restTemplate.exchange(url1, HttpMethod.GET, null, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            ExchangeRate exchangeRate = objectMapper.readValue(response1.getBody(), ExchangeRate.class);

            Map<String, BigDecimal> rates = exchangeRate.getRates();

            BigDecimal eurToUsd = rates.get("USD");
            BigDecimal eurToMnt = rates.get("MNT");

            globalVariables.setRatio(eurToMnt.divide(eurToUsd, RoundingMode.HALF_UP));

            log.info("updated currency ratio : {}", globalVariables.getRatio());
            return globalVariables.getRatio();

        } catch (Exception e) {
            log.error("failed to get currency converter ratio");
            throw new ValidationException("");
        }

    }

}
