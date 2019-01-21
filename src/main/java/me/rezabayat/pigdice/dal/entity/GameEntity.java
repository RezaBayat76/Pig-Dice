package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "GAME")
@Entity
@Data
public class GameEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long maxScore;

    private Long fallDice;

    private Long numDice;

    private Long maxThrow;

    private Long averageScore;

    private Long numPlayed;

    private Date createDate;

    private Long numPlayerScore;

    @ManyToOne
    @JsonManagedReference
    private UserEntity userCreator;

    @OneToMany(mappedBy = "game")
    @JsonBackReference
    private List<PlayedGameEntity> playedGames;

    @OneToMany(mappedBy = "game")
    @JsonBackReference
    private List<GameCommentEntity> gameComments;
}
