package com.InmersoAI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.InmersoAI.app.TareaAppService;
import com.InmersoAI.domain.Tarea;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class ApiController {
    private final TareaAppService tareaService;
    
    public ApiController(TareaAppService tareaService) {
        this.tareaService = tareaService;
    }
    
    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@RequestBody String descripcion) { //Interface creada
        Tarea tarea = tareaService.crearTarea(descripcion);
        return ResponseEntity.ok(tarea);
    }
    
    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodas() { 
        return ResponseEntity.ok(tareaService.obtenerTodasLasTareas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerTarea(@PathVariable Long id) { //Interface creada
        return tareaService.obtenerTarea(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Tarea> completarTarea(@PathVariable Long id) {
        return tareaService.completarTarea(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/tiempo")
    public ResponseEntity<Tarea> agregarTiempo(
            @PathVariable Long id,
            @RequestParam(defaultValue = "15") int minutos
    ) {
        return tareaService.agregarTiempo(id, minutos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }
}