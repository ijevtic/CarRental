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

    @Modifying
    @Query("update User u set u.email = :email, u.username = :username, u.firstName = :firstName, u.lastName = :lastName, u.password = :password, u.phoneNumber = :phoneNumber, u.birthDate = :birthDate, u.passportNumber = :passportNumber where u.id = :id")
    void updateUser(Long id, String email, String username, String firstName, String lastName, String password,
                    String phoneNumber, Integer birthDate, String passportNumber);

    @Modifying
    @Query("update User u set u.totalRentDays = :totalRentDays where u.id = :id")
    void updateRentDays(Long id, Integer totalRentDays);

    @Modifying
    @Query("update User u set u.email = :email, u.username = :username, u.firstName = :firstName, u.lastName = :lastName, u.password = :password, u.phoneNumber = :phoneNumber, u.birthDate = :birthDate, u.startWorkDate = :startWorkDate where u.id = :id")
    void updateManager(Long id, String email, String username, String firstName, String lastName, String password,
                    String phoneNumber, Integer birthDate, String startWorkDate);

    @Query("select p from User p where p.username = :usernameOrEmail or p.email = :usernameOrEmail")
    Optional<User> findUserByUsernameOrEmail(String usernameOrEmail);

    Optional<User> findUserByCompanyId(Long companyId);

    Optional<User> findUserByUsername(String username);
}

