package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class CommentOnGameDTO {

    private Long id;

    private Long score;

    private String text;

    private Long gameId;
}
