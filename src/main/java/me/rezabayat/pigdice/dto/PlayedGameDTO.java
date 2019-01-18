package me.rezabayat.pigdice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PlayedGameDTO {

    private Long id;

    private UserDTO firstPlayer;

    private UserDTO secondPlayer;

    private UserDTO winner;

    private Long score;

    private Date createDate;

    private GameDTO game;
}
