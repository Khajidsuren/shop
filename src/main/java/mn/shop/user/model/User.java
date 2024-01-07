package mn.shop.user.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_id",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String username;
    private String email;
    private Date lastLogin;
    private String password;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    public User(CreateUserDTO dto) {
        this.role = Objects.isNull(dto.getRole()) ? UserRole.USER : dto.getRole();
        this.email = dto.getEmail();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.isActive = true;
        this.createdAt = new Date();
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User {" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", lastLogin=" + lastLogin +
               ", password='" + password + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + new Date() +
               ", role=" + role +
               '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
