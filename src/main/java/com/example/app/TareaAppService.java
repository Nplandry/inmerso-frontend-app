package com.example.app;

import org.springframework.stereotype.Service;
import com.example.domain.Tarea;
import com.example.domain.TareaRestPort;

import java.util.List;
import java.util.Optional;

@Service
public class TareaAppService {
    private final TareaRestPort tareaPort;
    
    public TareaAppService(TareaRestPort tareaPort) {
        this.tareaPort = tareaPort;
    }

    
    public Tarea crearTarea(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        Tarea tarea = new Tarea(descripcion.trim());
        return tareaPort.guardar(tarea);
    }
    
    public Optional<Tarea> obtenerTarea(Long id) {
        return tareaPort.buscarPorId(id);
    }

    public List<Tarea> obtenerTodasLasTareas() {
        return tareaPort.obtenerTodas();
    }
    
    public Optional<Tarea> agregarTiempo(Long id, int minutos) {
        return tareaPort.buscarPorId(id)
                .map(tarea -> {
                    tarea.agregarTiempo(minutos);
                    return tareaPort.guardar(tarea);
                });
    }

    public Optional<Tarea> completarTarea(Long id) {
        return tareaPort.buscarPorId(id)
                .map(tarea -> {
                    tarea.completar();
                    return tareaPort.guardar(tarea);
                });
    }

    public void eliminarTarea(Long id) {
        tareaPort.eliminarPorId(id);
    }
    
    public long contarTareas() {
        return tareaPort.contar();
    }
    
    public boolean existeTarea(Long id) {
        return tareaPort.existePorId(id);
    }
}