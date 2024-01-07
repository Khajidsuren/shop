package mn.shop.user.controller;


import lombok.extern.slf4j.Slf4j;
import mn.shop.user.model.CreateUserDTO;
import mn.shop.user.model.User;
import mn.shop.user.model.UserView;
import mn.shop.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("create")
    public ResponseEntity<User> createUser(@RequestBody @Validated CreateUserDTO dto) {
        log.info("start to create user dto : {}", dto.toString());
        try {
            return ResponseEntity.ok(userService.createUser(dto));
        } catch (Exception e) {
            log.error("failed to create user username: {}, error: {}", dto.getUsername(), e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("list")
    public ResponseEntity<List<UserView>> list() {
        log.info("start to get user list");
        try {
            return ResponseEntity.ok(userService.list());
        } catch (Exception e) {
            log.error("failed to get user list error: {}", e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("update/{id:[0-9]+}")
    public ResponseEntity<User> update(@PathVariable("id") Long userId, @RequestBody CreateUserDTO dto) {
        log.info("start to update user id: {}", dto.toString());
        try {
            return ResponseEntity.ok(userService.updateUser(userId, dto));
        } catch (Exception e) {
            log.error("failed to update user id: {}, error: {}", userId, e.getMessage());
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/delete/{id:[0-9]+}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long userId) {
        log.info("Start to delete user id: {}", userId);
        try {
            return ResponseEntity.ok(userService.delete(userId));
        } catch (Exception e) {
            log.error("failed to delete user id: {}, error: {}", userId, e.getMessage());
            throw e;
        }
    }

}

