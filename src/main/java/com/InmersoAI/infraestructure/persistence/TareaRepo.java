package com.example.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepo extends JpaRepository<TareaEntity, Long> {
    // JpaRepository ya proporciona:
    // - save(TareaEntity tarea)
    // - findById(Long id)
    // - findAll()
    // - delete(TareaEntity tarea)
    // - deleteById(Long id)
    // - count()
    // - existsById(Long id)
}