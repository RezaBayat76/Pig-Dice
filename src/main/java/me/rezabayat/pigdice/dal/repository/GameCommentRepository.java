package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.GameCommentEntity;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameCommentRepository extends JpaRepository<GameCommentEntity, Long> {
    @Query("select p from GameCommentEntity p where p.accepted is null")
    List<GameCommentEntity> findAllUncheckedComments();
}
