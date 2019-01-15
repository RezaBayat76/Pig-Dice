package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}