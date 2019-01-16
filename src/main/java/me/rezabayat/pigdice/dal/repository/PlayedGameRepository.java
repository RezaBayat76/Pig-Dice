package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedGameRepository extends JpaRepository<PlayedGameEntity, Long> {
}
