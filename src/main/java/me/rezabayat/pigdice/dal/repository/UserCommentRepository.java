package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.UserCommentEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Long> {

    @Query("select p from UserCommentEntity p where p.accepted = true and p.mentionUser = :mentionUser")
    List<UserCommentEntity> findAllByMentionUser(@Param("mentionUser") UserEntity mentionUser);

    @Query("select p from UserCommentEntity p where p.accepted is null")
    List<UserCommentEntity> uncheckedComments();
}
