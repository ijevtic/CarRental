package org.example.repository;

import org.example.domain.User;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    @Modifying
    @Query("update User u set u.enabled = true where u.email like :email")
    void enableUser(String email);

    @Query("select p from User p where p.username like :username or p.email like :email")
    Optional<User> findUserByUsernameOrEmail(String username, String email);
}

