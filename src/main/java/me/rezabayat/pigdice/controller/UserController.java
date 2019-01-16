package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.UserDTO;
import me.rezabayat.pigdice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public void register(@RequestBody UserDTO userDTO){
        this.userService.createNewUser(userDTO);
    }
}
