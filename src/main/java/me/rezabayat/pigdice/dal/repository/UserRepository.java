package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
}
