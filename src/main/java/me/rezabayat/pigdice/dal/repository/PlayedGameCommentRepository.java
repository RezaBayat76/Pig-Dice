package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.PlayedGameCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedGameCommentRepository extends JpaRepository<PlayedGameCommentEntity, Long> {
}
