package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "PLAYED_GAME")
@Entity
@Data
public class PlayedGameEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private UserEntity firstPlayer;

    @OneToOne
    private UserEntity secondPlayer;

    @OneToOne
    private UserEntity winner;

    private Long score;

    private Date createDate;

    @ManyToOne
    @JsonManagedReference
    private GameEntity game;

    @OneToMany(mappedBy = "playedGame")
    @JsonBackReference
    private List<PlayedGameCommentEntity> playedGameComments;
}
