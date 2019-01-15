package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.UserCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Long>{
}
