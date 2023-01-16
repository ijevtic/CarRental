package org.example.repository;

import org.example.domain.Role;
import org.example.domain.State;
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
    @Query("update User u set u.state = :state where u.email like :usernameOrEmail or u.username like :usernameOrEmail")
    void updateState(String usernameOrEmail, State state);

    @Query("select p from User p where p.username like :usernameOrEmail or p.email like :usernameOrEmail")
    Optional<User> findUserByUsernameOrEmail(String usernameOrEmail);

    Optional<User> findUserByUsername(String username);
}

