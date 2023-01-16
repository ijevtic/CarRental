package org.example.repository;

import org.example.domain.EState;
import org.example.domain.Role;
import org.example.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findStateByName(String name);

    Optional<State> getReferenceById(Long id);
}