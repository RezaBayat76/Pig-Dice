package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Table(name = "PLAYED_GAME_COMMENT")
@Entity
@Data
public class PlayedGameCommentEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long score;

    private String text;

    private Boolean accepted;

    @ManyToOne
    @JsonManagedReference
    private PlayedGameEntity playedGame;

    private UserEntity user;
}
