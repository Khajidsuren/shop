package mn.shop.user.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.Date;

@RequestMapping("update-password")
@Entity
@Table(name = "update_password")
public class UpdatePassword {
    @SequenceGenerator(
            name = "upp_sequence",
            sequenceName = "upp_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "upp_sequence"
    )
    private Long id;
    private String code;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;


    public UpdatePassword(String code, String username) {
        this.code = code;
        this.username = username;
        this.createdAt = new Date();
    }

    protected UpdatePassword() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
