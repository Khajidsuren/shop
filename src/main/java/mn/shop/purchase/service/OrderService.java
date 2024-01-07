package mn.shop.purchase.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import mn.shop.purchase.model.*;
import mn.shop.purchase.repository.OrderRepository;
import mn.shop.utils.HttpClient;
import mn.shop.utils.ValidationException;
import okhttp3.Headers;
import okhttp3.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ObjectMapper objectMapper;
    private HttpClient httpClient;

    public OrderService(OrderRepository repository, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = new HttpClient("https://merchant.qpay.mn/v2");
        this.repository = repository;
    }

    public Order createOrder(OrderCreateDTO dto) {
        return repository.save(new Order(dto));
    }

    public Order updateState(Long id, OrderState state) {

        Order order = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Захиалга олдсонгүй"));


        if (OrderState.PAID.equals(state)) {
            String token = getAccessToken();
            PaymentData paymentData = checkPayment(token, order.getQpayInvoiceId());
            if (!paymentData.getRows().isEmpty() && paymentData.getPaidAmount() >= order.getPrice()) {
                order.setOrderState(OrderState.PAID);
                return repository.save(order);
            }
        }

        if (OrderState.CANCELLED.equals(state)) {
            String token = getAccessToken();
            PaymentData paymentData = checkPayment(token, order.getQpayInvoiceId());
            if (paymentData.getRows().isEmpty()) {
                order.setOrderState(OrderState.CANCELLED);
                return repository.save(order);
            }
        }

        order.setOrderState(state);
        return repository.save(order);

    }

    public boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    public Order findById(Long orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Захилага олдсонгүй"));
    }

    public void save(Order order) {
        repository.save(order);
    }

    public Page<OrderView> search(String phone, String address, String product, OrderState state, String info, Double minPrice, Double maxPrice, Date from, Date to, Integer page, Integer size) {
        return repository.search(StringUtils.hasText(phone) ? phone.toLowerCase() : null,
                StringUtils.hasText(address) ? address.toLowerCase() : null,
                StringUtils.hasText(product) ? product.toLowerCase() : null,
                state,
                StringUtils.hasText(info) ? info.toLowerCase() : null,
                minPrice,
                maxPrice,
                from,
                to,
                PageRequest.of(page, size));
//        return repository.getList(PageRequest.of(page, size));
    }

    public List<OrderView> search(String phone, String address, String product, OrderState state, String info, Double minPrice, Double maxPrice, Date from, Date to) {
        return repository.search(StringUtils.hasText(phone) ? phone.toLowerCase() : null,
                StringUtils.hasText(address) ? address.toLowerCase() : null,
                StringUtils.hasText(product) ? product.toLowerCase() : null,
                state,
                StringUtils.hasText(info) ? info.toLowerCase() : null,
                minPrice,
                maxPrice,
                from,
                to);
//        return repository.getList(PageRequest.of(page, size));
    }

    public Order findByQpayInvoiceId(String invoiceId) {
        return repository.findByQpayInvoiceId(invoiceId)
                .orElseThrow(() -> new ValidationException("Захилага олдсонгүй"));
    }


    //    TODO : implement this code
    public void updateOrderState() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 2);

        Date beginDate = calendar.getTime();

        String token = getAccessToken();
        List<Order> orders = repository.findByStateAndCreatedAt(OrderState.CREATED, beginDate);
        orders.forEach(order -> {
            PaymentData paymentData = checkPayment(token, order.getQpayInvoiceId());
            if (!paymentData.getRows().isEmpty() && paymentData.getPaidAmount() >= order.getPrice()) {
                order.setOrderState(OrderState.PAID);
            } else order.setOrderState(OrderState.CANCELLED);
            save(order);
        });
    }

    public String getAccessToken() {

        String credentials = "TSOTAN:MM05C9xr";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        RestTemplate restTemplate = new RestTemplate();
        // Provide the correct request body as the first parameter
        ResponseEntity<AccessTokenResponse> responseEntity = restTemplate.postForEntity(
                "https://merchant.qpay.mn/v2/auth/token",
                new HttpEntity<>(headers),
                AccessTokenResponse.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody().getAccess_token();
        } else {
            throw new ValidationException("Failed to get access token: " + responseEntity.getStatusCode());
        }
    }

    public PaymentData checkPayment(String accessToken, String invoiceNumber) {
        try {

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            headers.put("Content-Type", "application/json; charset=utf-8");
            Response response = httpClient.post("/payment/check", new QpayCheckPaymentRequest(invoiceNumber), Headers.of(headers));
            String resString = response.body().string();
            response.close();
            if (response.isSuccessful()) {
                return objectMapper.readValue(resString, PaymentData.class);
            } else {
                throw new ValidationException("failed");
            }
        } catch (Exception e) {
            throw new ValidationException("failed");
        }
    }

    public InvoiceResponse createInvoice(OrderCreateDTO orderCreateDTO) {

        String accessToken = getAccessToken();

        try {

            Order order = new Order(orderCreateDTO);
            save(order);

//            String unique = String.valueOf(System.currentTimeMillis());

            String unique = "#" + order.getId();
            QpayInvoiceRequest requestDTO = new QpayInvoiceRequest(unique, order.getPrice());

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            headers.put("Content-Type", "application/json; charset=utf-8");
            Response response = httpClient.post("/invoice", requestDTO, Headers.of(headers));
            String resString = response.body().string();
            response.close();

            if (response.isSuccessful()) {
                QpayInvoiceResponse qpayInvoiceResponse = objectMapper.readValue(resString, QpayInvoiceResponse.class);
                order.setTransactionInfo("TSOTAN " + unique);
                order.setQpayInvoiceId(qpayInvoiceResponse.getInvoiceId());
                order.setOrderState(OrderState.CREATED);
                save(order);
                return new InvoiceResponse(qpayInvoiceResponse.getQrImage(), qpayInvoiceResponse.getQrText(),
                        qpayInvoiceResponse.getInvoiceId(), qpayInvoiceResponse.getqPayShortUrl(), unique);
            } else {
                throw new ValidationException("failed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public Order checkIsPaid(Long id) {
        Order order = findById(id);
        PaymentData paymentData = checkPayment(getAccessToken(), order.getQpayInvoiceId());
        if (!paymentData.getRows().isEmpty() && paymentData.getPaidAmount() >= order.getPrice()) {
            order.setOrderState(OrderState.PAID);
        } else order.setOrderState(OrderState.CANCELLED);
        save(order);
        return order;
    }

}
