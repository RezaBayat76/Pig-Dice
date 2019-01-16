package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.LoginDTO;
import me.rezabayat.pigdice.dto.UserCommentDTO;
import me.rezabayat.pigdice.dto.UserDTO;
import me.rezabayat.pigdice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("authenticate")
    public UserDTO authenticate(@RequestBody LoginDTO loginDTO){
        return this.userService.authenticate(loginDTO);
    }

    @GetMapping
    public List<UserDTO> users(){
        return this.userService.allUsers();
    }

    @GetMapping("comments/{id}")
    public List<UserCommentDTO> comments(@PathVariable("id") long id){
        return this.userService.commentOnUser(id);
    }
}
