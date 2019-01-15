package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Table(name = "USER_COMMENT")
@Entity
@Data
public class UserCommentEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long score;

    private String text;

    private Boolean accepted;

    @OneToOne
    private UserEntity userCreator;

    @ManyToOne
    @JsonManagedReference
    private UserEntity mentionUser;
}

