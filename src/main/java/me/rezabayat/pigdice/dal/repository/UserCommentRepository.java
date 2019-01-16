package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.UserCommentEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Long> {

    List<UserCommentEntity> findAllByMentionUser(@Param("mentionUser") UserEntity mentionUser);
}
