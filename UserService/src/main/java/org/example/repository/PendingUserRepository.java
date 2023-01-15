package org.example.repository;

import org.example.domain.PendingUser;
import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findPendingUserByEmailAndVerificationCode(String email, String verificationCode);

    void deletePendingUserByEmail(String email);
}
