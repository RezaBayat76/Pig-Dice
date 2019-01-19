package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class UpdatedScoreDTO {
    private long gameId;
    private long score;
    private long playedGameId;
}
