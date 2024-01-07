package mn.shop.user.service;

import mn.shop.user.model.CreateUserDTO;
import mn.shop.user.model.User;
import mn.shop.user.model.UserView;
import mn.shop.user.repository.UserRepository;
import mn.shop.utils.ValidationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUserName(String username) {
        return userRepository.findAllUser()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Хэрэглэгч олдсонгүй"));
    }

//    public UserDetails loadUserByUsername(String email)  {
//        User user = userRepository.findUserByEmail(email)
//                .orElseThrow(() ->  new ValidationException("Хэрэглэгч олдсонгүй"));
//
//        // You might need to convert your User entity to UserDetails here
//        // (e.g., using org.springframework.security.core.userdetails.User)
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .roles(user.getRole().name())  // Assuming UserRole is an enum
//                .build();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ValidationException("Хэрэглэгч олдсонгүй"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public User createUser(CreateUserDTO createUserDTO) {
        User user = new User(createUserDTO);
        return userRepository.save(user);
    }

    public User setLoginTime(User user) {
        user.setLastLogin(new Date());
        return userRepository.save(user);
    }

    public List<UserView> list() {
        return userRepository.findAllUserView(Boolean.TRUE);
    }

    public User updateUser(Long userId, CreateUserDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Хэрэглэгч олдсонгүй"));
        if (StringUtils.hasText(dto.getEmail())) user.setEmail(dto.getEmail());
        if (StringUtils.hasText(dto.getUsername())) user.setUsername(dto.getUsername());
        if (StringUtils.hasText(dto.getPassword())) user.setPassword(dto.getPassword());
        return userRepository.save(user);
    }

    public Boolean delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Хэрэглэгч олдсонгүй"));
        user.setActive(false);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
        return Boolean.TRUE;
    }

    public int generateOtp() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        return random.nextInt(max - min + 1) + min;
    }

}
