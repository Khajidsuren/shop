package mn.shop.purchase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QpayInvoiceRequest implements Serializable {

    @JsonProperty("invoice_code")
    protected String invoiceCode;
    @JsonProperty("sender_invoice_no")
    protected String senderInvoiceNo;
    @JsonProperty("invoice_receiver_code")
    protected String invoiceReceiverCode;
    @JsonProperty("invoice_description")
    protected String invoiceDescription;
    @JsonProperty("amount")
    protected Double amount;
    @JsonProperty("callback_url")
    protected String callbackUrl;

    public QpayInvoiceRequest(String invoiceCode,
                              String senderInvoiceNo,
                              String invoiceReceiverCode,
                              String invoiceDescription,
                              Double amount,
                              String callbackUrl) {
        this.invoiceCode = invoiceCode;
        this.senderInvoiceNo = senderInvoiceNo;
        this.invoiceReceiverCode = invoiceReceiverCode;
        this.invoiceDescription = invoiceDescription;
        this.amount = amount;
        this.callbackUrl = callbackUrl;
    }


    public QpayInvoiceRequest(String senderInvoiceNo,
                              Double amount) {
        this.invoiceCode = "TSOTAN_INVOICE";
        this.senderInvoiceNo = senderInvoiceNo;
        this.invoiceReceiverCode = "PR" + senderInvoiceNo;
        this.invoiceDescription = "TSOTAN " + senderInvoiceNo;
        this.amount = amount;
        this.callbackUrl = "https://bd5492c3ee85.ngrok.io/payments?payment_id=" + senderInvoiceNo;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getSenderInvoiceNo() {
        return senderInvoiceNo;
    }

    public void setSenderInvoiceNo(String senderInvoiceNo) {
        this.senderInvoiceNo = senderInvoiceNo;
    }

    public String getInvoiceReceiverCode() {
        return invoiceReceiverCode;
    }

    public void setInvoiceReceiverCode(String invoiceReceiverCode) {
        this.invoiceReceiverCode = invoiceReceiverCode;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
