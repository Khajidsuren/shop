package mn.shop.purchase.model;


import lombok.Getter;

@Getter
public class InvoiceResponse {

    private String qpayUrl;
    private String qpayText;
    private String invoiceId;
    private String qpayShortUrl;
    private String transactionInfo;

    public InvoiceResponse(String qpayUrl, String qpayText, String invoiceId, String qpayShortUrl, String unique) {
        this.qpayUrl = qpayUrl;
        this.qpayText = qpayText;
        this.invoiceId = invoiceId;
        this.qpayShortUrl = qpayShortUrl;
        this.transactionInfo = unique;
    }

}
