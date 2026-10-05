package com.example.app;

import org.springframework.stereotype.Service;
import com.example.domain.Tarea;
import com.example.infraestructure.persistence.TareaEntity;
import com.example.infraestructure.persistence.TareaRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TareaAppService {
    private final TareaRepo tareaRepo;
    
    public TareaAppService(TareaRepo tareaRepo) {
        this.tareaRepo = tareaRepo;
    }

    private Tarea toDomain(TareaEntity entity) {
    return new Tarea(
        entity.getId(),
        entity.getDescripcion(),
        entity.isCompletada(),
        entity.getHoraCreacion(),
        entity.getTiempoRestanteMinutos()
    );
}

    
    public Tarea crearTarea(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        Tarea tarea = new Tarea(descripcion.trim());
        return toDomain(tareaRepo.save(toEntity(tarea)));
    }
    
    public Optional<Tarea> obtenerTarea(Long id) {
        return tareaRepo.findById(id).map(this::toDomain);
    }
    
    public List<Tarea> obtenerTodasLasTareas() {
        return tareaRepo.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    public Optional<Tarea> agregarTiempo(Long id, int minutos) {
        return tareaRepo.findById(id)
                .map(this::toDomain)
                .map(tarea -> {
                    tarea.agregarTiempo(minutos);
                    return toDomain(tareaRepo.save(toEntity(tarea)));
                });
    }

    public Optional<Tarea> completarTarea(Long id) {
        return tareaRepo.findById(id)
                .map(this::toDomain)
                .map(tarea -> {
                    tarea.completar();
                    return toDomain(tareaRepo.save(toEntity(tarea)));
                });
    }

    public void eliminarTarea(Long id) {
        tareaRepo.deleteById(id);
    }
    
    public long contarTareas() {
        return tareaRepo.count();
    }
    
    public boolean existeTarea(Long id) {
        return tareaRepo.existsById(id);
    }
    
    private TareaEntity toEntity(Tarea tarea) {
    TareaEntity entity = new TareaEntity();
    if (tarea.getId() != null) {
        entity.setId(tarea.getId());
    }
        entity.setDescripcion(tarea.getDescripcion());
        entity.setCompletada(tarea.isCompletada());
        entity.setHoraCreacion(tarea.getHoraCreacion());
        entity.setTiempoRestanteMinutos(tarea.getTiempoRestanteMinutos());
        return entity;
    }
    
}