package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}