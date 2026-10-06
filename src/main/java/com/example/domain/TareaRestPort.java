package com.example.domain;
import java.util.Optional;
import java.util.List;


public interface TareaRestPort {
    Tarea guardar(Tarea tarea);
    Optional<Tarea> buscarPorId(Long id);
    List<Tarea> obtenerTodas();
    void eliminarPorId(Long id);
    Long Contar();
    boolean existePorId(Long id);   
}