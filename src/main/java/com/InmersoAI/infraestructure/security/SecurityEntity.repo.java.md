package com.InmersoAI.infraestructure.security;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityEntityRepository extends JpaRepository<SecurityEntity, Long> {
    Optional<SecurityEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}