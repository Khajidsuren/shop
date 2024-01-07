package mn.shop.purchase.model;

import lombok.Data;

@Data
public class AccessTokenResponse {

    private String token_type;
    private long refresh_expires_in;
    private String refresh_token;
    private String access_token;
    private long expires_in;
    private String scope;
    private String not_before_policy;
    private String session_state;


}
