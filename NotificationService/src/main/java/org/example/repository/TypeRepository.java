package org.example.repository;

import org.example.domain.Notification;
import org.example.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> getReferenceById(Long id);

    Optional<Type> findTypeByName(String name);

}
