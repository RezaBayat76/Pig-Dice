package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Query("select p from GameEntity p order by p.numPlayed DESC")
    List<GameEntity> mostPlaying();

    @Query("select p from GameEntity p order by p.createDate ASC")
    List<GameEntity> bestRecently();

    @Query("select p from GameEntity p order by p.averageScore DESC")
    List<GameEntity> bestGames();

    @Query("select p from GameEntity p where p.userCreator = :userEntity")
    List<GameEntity> designedGames(@Param("userEntity") UserEntity userEntity);

}