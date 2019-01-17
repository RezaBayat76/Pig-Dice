package me.rezabayat.pigdice.dal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Table(name = "FOLLOWING")
@Entity
@Data
public class FollowingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonManagedReference
    private UserEntity user;

    @ManyToOne
    @JsonManagedReference
    private UserEntity followedUser;

}
