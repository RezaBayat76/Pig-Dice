package me.rezabayat.pigdice.dal.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "PLAYED_GAME")
@Entity
@Data
public class PlayedGameEntity {

    @Id
    @GeneratedValue
    private Long id;

    private UserEntity firstPlayer;

    private UserEntity secondPlayer;

    private UserEntity winner;

    private Long score;

    private Date createDate;

    @ManyToOne
    @JsonManagedReference
    private GameEntity game;
}
