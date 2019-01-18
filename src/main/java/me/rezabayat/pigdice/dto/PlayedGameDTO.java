package me.rezabayat.pigdice.dto;

import java.util.Date;

public class PlayedGameDTO {

    private Long id;

    private UserDTO firstPlayer;

    private UserDTO secondPlayer;

    private UserDTO winner;

    private Long score;

    private Date createDate;

    private GameDTO game;
}
