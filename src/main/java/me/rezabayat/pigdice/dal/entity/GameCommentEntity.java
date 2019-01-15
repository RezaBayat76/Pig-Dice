package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Table(name = "GAME_COMMENT")
@Entity
@Data
public class GameCommentEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long score;

    private String text;

    @ManyToOne
    @JsonManagedReference
    private GameEntity game;

    private UserEntity user;
}
