package mn.shop.purchase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data

public class QpayInvoiceResponse {

    @JsonProperty("invoice_id")
    protected String invoiceId;
    @JsonProperty("qr_text")
    protected String qrText;
    @JsonProperty("qr_image")
    protected String qrImage;
    @JsonProperty("qPay_shortUrl")
    protected String qPayShortUrl;
    @JsonProperty("urls")
    protected List<URLS> urls;

    public QpayInvoiceResponse() {
        // Default constructor
    }


    public QpayInvoiceResponse(String invoiceId, String qrText, String qrImage, String qPayShortUrl, List<URLS> urls) {
        this.invoiceId = invoiceId;
        this.qrText = qrText;
        this.qrImage = qrImage;
        this.qPayShortUrl = qPayShortUrl;
        this.urls = urls;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public String getqPayShortUrl() {
        return qPayShortUrl;
    }

    public void setqPayShortUrl(String qPayShortUrl) {
        this.qPayShortUrl = qPayShortUrl;
    }

    public List<URLS> getUrls() {
        return urls;
    }

    public void setUrls(List<URLS> urls) {
        this.urls = urls;
    }

    public static class URLS {
        @JsonProperty("name")
        protected String name;
        @JsonProperty("description")
        protected String description;
        @JsonProperty("logo")
        protected String logo;
        @JsonProperty("link")
        protected String link;

        public URLS() {
        }

        public String getName() {
            return this.name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return this.description;
        }

        @JsonProperty("description")
        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return this.logo;
        }

        @JsonProperty("logo")
        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLink() {
            return this.link;
        }

        @JsonProperty("link")
        public void setLink(String link) {
            this.link = link;
        }
    }


}
