package com.example.infraestructure.persistence;

import com.example.domain.Tarea;
import com.example.domain.TareaRestPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TareaAdapter implements TareaRestPort {

    private final TareaRepo tareaRepo;

    public TareaAdapter(TareaRepo tareaRepo) {
        this.tareaRepo = tareaRepo;
    }

    @Override
    public Tarea guardar(Tarea tarea) {
        TareaEntity entity = toEntity(tarea);
        return toDomain(tareaRepo.save(entity));
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return tareaRepo.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarPorId(Long id) {
        tareaRepo.deleteById(id);
    }

    @Override
    public Long Contar() {
        return tareaRepo.count();
    }

    @Override
    public boolean existePorId(Long id) {
        return tareaRepo.existsById(id);
    }

    // Mappers de conversión
    private Tarea toDomain(TareaEntity entity) {
        return new Tarea(
                entity.getId(),
                entity.getDescripcion(),
                entity.isCompletada(),
                entity.getHoraCreacion(),
                entity.getTiempoRestanteMinutos()
        );
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