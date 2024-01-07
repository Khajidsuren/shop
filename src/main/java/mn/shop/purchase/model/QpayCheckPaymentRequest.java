package mn.shop.purchase.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QpayCheckPaymentRequest {
    @JsonProperty("object_type")
    protected String objectType;
    @JsonProperty("object_id")
    protected String objectId;

    public QpayCheckPaymentRequest(String objectId) {
        this.objectType = "INVOICE";
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
