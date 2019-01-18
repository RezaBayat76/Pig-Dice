package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.*;
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
    public void register(@RequestBody UserDTO userDTO) {
        this.userService.createNewUser(userDTO);
    }

    @PostMapping("authenticate")
    public UserDTO authenticate(@RequestBody LoginDTO loginDTO) {
        return this.userService.authenticate(loginDTO);
    }

    @GetMapping
    public List<UserDTO> users() {
        return this.userService.allUsers();
    }

    @GetMapping("comments/{id}")
    public List<UserCommentDTO> comments(@PathVariable("id") long id) {
        return this.userService.commentOnUser(id);
    }

    @PostMapping("add-comment")
    public void addComment(@RequestBody CommentOnUserDTO commentOnUserDTO, @RequestHeader("Authorization") String token) {
        this.userService.addComment(commentOnUserDTO, token);
    }

    @GetMapping("follow/{id}")
    public void follow(@PathVariable("id") long id, @RequestHeader("Authorization") String token){
        this.userService.follow(id, token);
    }

    @GetMapping("unfollow/{id}")
    public void unFollow(@PathVariable("id") long id, @RequestHeader("Authorization") String token){
        this.userService.unFollow(id, token);
    }

    @GetMapping("followings")
    public List<Long> followings(@RequestHeader("Authorization") String token){
        return this.userService.followings(token);
    }

    @GetMapping("profile")
    public UserProfileDTO profile(@RequestHeader("Authorization") String token){
        return this.userService.profile(token);
    }

    @PutMapping("edit-profile")
    public void editProfile(@RequestBody UserDTO userDTO){
        this.userService.editProfile(userDTO);
    }
}
