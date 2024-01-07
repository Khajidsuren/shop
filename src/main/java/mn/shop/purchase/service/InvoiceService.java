package mn.shop.purchase.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mn.shop.purchase.model.*;
import mn.shop.utils.HttpClient;
import mn.shop.utils.ValidationException;
import okhttp3.Headers;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;

@Service
@Slf4j
public class InvoiceService {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private HttpClient httpClient;


    public InvoiceService(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.httpClient = new HttpClient("https://merchant.qpay.mn/v2");
        this.objectMapper = objectMapper;
    }


//    public String getAccessToken() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // Create a request body with grant_type, username, and password
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
////        body.add("grant_type", "11D563FC7E64FC2E430E2EFB40C48465");
//        body.add("Username", "TSOTAN");
//        body.add("Password", "MM05C9xr");
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<AccessTokenResponse> responseEntity = restTemplate.exchange(
//                "https://merchant.qpay.mn/v2/auth/token",
//                HttpMethod.POST,
//                requestEntity,
//                AccessTokenResponse.class
//        );
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            AccessTokenResponse accessTokenResponse = responseEntity.getBody();
//            return accessTokenResponse.getAccess_token();
//        } else {
//            throw new RuntimeException("Failed to obtain access token: " + responseEntity.getStatusCode());
//        }
//    }

//    public String getAccessToken() {
//        try {
//            String credentials = "TSOTAN:MM05C9xr";
//            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
//
//            HashMap<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "Basic " + encodedCredentials);
//            headers.put("Content-Type", "application/json; charset=utf-8");
//            Response response = httpClient.post("/auth/token", Headers.of(headers));
//            String resString = response.body().string();
//            response.close();
//            if (response.isSuccessful()) {
//                return objectMapper.readValue(resString, AccessTokenResponse.class).getAccess_token();
//            } else {
//                throw new ValidationException("failed");
//            }
//        } catch (Exception e) {
//            throw new ValidationException("failed");
//        }
//    }

    //    it works
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


    //    Test endpoint
//    public QpayInvoiceResponse createInvoice(QpayInvoiceRequest requestDTO) {
//
//        String accessToken = getAccessToken();
//
//        try {
//
//            HashMap<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "Bearer " + accessToken);
//            headers.put("Content-Type", "application/json; charset=utf-8");
//            Response response = httpClient.post("/invoice", requestDTO, Headers.of(headers));
//            String resString = response.body().string();
//            response.close();
//            if (response.isSuccessful()) {
//                return objectMapper.readValue(resString, QpayInvoiceResponse.class);
//            } else {
//                throw new ValidationException("failed");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

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
            throw new RuntimeException(e);
        }
    }


    public InvoiceResponse createInvoice(OrderCreateDTO orderCreateDTO) {

        String accessToken = getAccessToken();

        try {

            Order order = new Order(orderCreateDTO);
            orderService.save(order);

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
                orderService.save(order);
                return new InvoiceResponse(qpayInvoiceResponse.getQrImage(), qpayInvoiceResponse.getQrText(),
                        qpayInvoiceResponse.getInvoiceId(), qpayInvoiceResponse.getqPayShortUrl(), unique);
            } else {
                throw new ValidationException("failed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

////
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<QpayInvoiceRequest> requestEntity = new HttpEntity<>(requestDTO, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<QpayInvoiceResponse> responseEntity = restTemplate.postForEntity("https://merchant.qpay.mn/v2/invoice", requestEntity, QpayInvoiceResponse.class);
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            return responseEntity.getBody();
//        } else {
//            throw new RuntimeException("Failed to create invoice: " + responseEntity.getStatusCode());
//        }
    }

}
