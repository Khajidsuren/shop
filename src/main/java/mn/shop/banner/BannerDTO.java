package mn.shop.banner;

public class BannerDTO {

    private String url;
    private String type;

    public BannerDTO(String url, String type) {
        this.url = url;
        this.type = type;
    }

    protected BannerDTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
