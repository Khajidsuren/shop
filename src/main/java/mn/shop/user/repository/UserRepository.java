package mn.shop.user.repository;

import mn.shop.user.model.User;
import mn.shop.user.model.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u")
    List<User> findAllUser();

    @Query("select new mn.shop.user.model.UserView(u.id, u.username, u.email, u.role, u.lastLogin, u.isActive, u.createdAt) " +
           "from User as u where u.isActive = :isActive")
    List<UserView> findAllUserView(Boolean isActive);

    Optional<User> findById(Long id);

    @Query("select u from User as u where u.username = :username")
    Optional<User> findUserByUsername(String username);

}
