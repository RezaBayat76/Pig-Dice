package me.rezabayat.pigdice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Date birthday;

    private String gender;

    private String username;

    private String token;
}
