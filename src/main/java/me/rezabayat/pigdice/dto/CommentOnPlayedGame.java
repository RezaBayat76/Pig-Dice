package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class CommentOnPlayedGame {

    private Long score;

    private String text;

    private Long playedGameId;
}