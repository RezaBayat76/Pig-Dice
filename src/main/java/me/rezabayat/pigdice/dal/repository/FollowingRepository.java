package me.rezabayat.pigdice.dal.repository;

import me.rezabayat.pigdice.dal.entity.FollowingEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FollowingRepository extends JpaRepository<FollowingEntity, Long> {

    @Modifying
    @Transactional
    void deleteByUserAndFollowedUser(@Param("user") UserEntity user, @Param("followedUser") UserEntity followedUser);
}
