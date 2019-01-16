package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class PlayedGameCommentDTO {

    private Long id;

    private Long score;

    private String text;

    private Boolean accepted;

    private UserDTO user;
}
