package me.rezabayat.pigdice.dal.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "USER")
@Entity
@Data
public class UserEntity {

    @Id
    private Long id;

    private String name;

    private String familyName;

    private Date birthday;

    private String gender;

    private String userName;

    private String password;
}
