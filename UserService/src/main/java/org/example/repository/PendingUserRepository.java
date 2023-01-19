package org.example.repository;

import org.example.domain.PendingUser;
import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findPendingUserByEmailAndVerificationCode(String email, String verificationCode);

    void deletePendingUserByEmail(String email);
}
