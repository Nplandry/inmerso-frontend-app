package com.example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepo extends JpaRepository<Todo, Long> {
    
}
/**
 * Detecta que es una interfaz de JPA, genera el bytecode/proxy dinámico con la implementación SQL en memoria y hace un new de esa clase.
 */