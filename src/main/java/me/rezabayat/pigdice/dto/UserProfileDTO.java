package me.rezabayat.pigdice.dto;

import lombok.Data;
import me.rezabayat.pigdice.enums.Role;

import java.util.Date;
import java.util.List;

@Data
public class UserProfileDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Date birthday;

    private String gender;

    private String username;

    private String password;

    private String email;

    private Role role;

    private List<GameDTO> games;

    private List<UserCommentDTO> userComments;

    private List<UserDTO> followings;

    private List<UserDTO> followers;
}
