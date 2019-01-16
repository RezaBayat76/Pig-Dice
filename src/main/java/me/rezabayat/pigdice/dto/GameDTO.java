package me.rezabayat.pigdice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GameDTO {

    private Long id;

    private Long maxScore;

    private Long fallDice;

    private Long numDice;

    private Long maxThrow;

    private Long averageScore;

    private Date createDate;

    private UserDTO userCreator;
}
