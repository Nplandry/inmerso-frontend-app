package com.InmersoAI.domain;
import java.util.Optional;
import java.util.List;


public interface TareaRestPort {
    Tarea guardar(Tarea tarea);
    Optional<Tarea> buscarPorId(Long id);
    List<Tarea> obtenerTodas();
    void eliminarPorId(Long id);
    Long contar();
    boolean existePorId(Long id);   
}