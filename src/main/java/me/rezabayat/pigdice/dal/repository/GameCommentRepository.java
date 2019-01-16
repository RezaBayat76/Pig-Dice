package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.GameCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCommentRepository extends JpaRepository<GameCommentEntity, Long> {
}
