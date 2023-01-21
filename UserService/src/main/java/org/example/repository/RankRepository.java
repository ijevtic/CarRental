package org.example.repository;

import org.example.domain.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<UserRank, Long> {

    @Query("select r from UserRank r where r.minDays <= :rentDays and r.maxDays >= :rentDays")
    Optional<UserRank> getRankForRentDaysNumber(Integer rentDays);
}
