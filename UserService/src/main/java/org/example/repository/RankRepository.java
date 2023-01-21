package org.example.repository;

import org.example.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

    @Query("select r from Rank r where r.minDays <= :rentDays and r.maxDays >= :rentDays")
    Optional<Rank> getRankForRentDaysNumber(Integer rentDays);
}
