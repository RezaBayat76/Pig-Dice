package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import me.rezabayat.pigdice.enums.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "USER")
@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String familyName;

    private Date birthday;

    private String gender;

    private String userName;

    private String password;

    @Enumerated
    private Role role;

    @OneToMany(mappedBy = "userCreator")
    @JsonBackReference
    private List<GameEntity> games;

    @OneToMany(mappedBy = "mentionUser")
    @JsonBackReference
    private List<UserCommentEntity> userComments;
}
