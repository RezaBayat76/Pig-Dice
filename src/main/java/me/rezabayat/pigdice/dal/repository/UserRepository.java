package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    Optional<UserEntity> findByUsername(@Param("username") String username);


}
