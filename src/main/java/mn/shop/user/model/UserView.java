package mn.shop.user.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;


@Getter
public class UserView implements Serializable {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private Date lastLogin;
    private Boolean isActive;
    private Date createdAt;

    public UserView(Long id, String username, String email, UserRole role, Date lastLogin, Boolean isActive, Date createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

}
