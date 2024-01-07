package mn.shop.banner;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "banner")
public class Banner implements Serializable {
    @SequenceGenerator(
            name = "banner_sequence",
            sequenceName = "banner_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "banner_sequence"
    )
    private Long id;

    private String url;
    private String type;
    private Date createdAt;

    protected Banner() {
    }

    public Banner(String url, String type) {
        this.url = url;
        this.type = type;
        this.createdAt = new Date();
    }

    public Banner(BannerDTO bannerDTO) {
        this.url = bannerDTO.getUrl();
        this.type = bannerDTO.getType();
        this.createdAt = new Date();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
