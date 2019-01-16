package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class UserCommentDTO {

    private Long id;

    private Long score;

    private String text;

    private Boolean accepted;

    private UserDTO userCreator;

    private UserDTO mentionUser;
}
