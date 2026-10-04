package com.example.domain;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity 
public class Tarea {
    @Id 
    @GeneratedValue 
    private Long id;
    private String descripcion;
    private boolean completada;
    private LocalTime horaCreacion;
    private int tiempoRestanteMinutos;
    
    private static final ZoneId CHILE_ZONE = ZoneId.of("America/Santiago");
    private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");
    
    public Tarea(String descripcion) {
        this.descripcion = descripcion;
        this.horaCreacion = LocalTime.now(CHILE_ZONE);
        this.tiempoRestanteMinutos = 60;
        this.completada = false;
    }   
    
    public Tarea(Long id, String descripcion, boolean completada, LocalTime horaCreacion, int tiempoRestanteMinutos) {
        this.id = id;
        this.descripcion = descripcion;
        this.completada = completada;
        this.horaCreacion = horaCreacion;
        this.tiempoRestanteMinutos = tiempoRestanteMinutos;
    }
    
    public Tarea() {
    }
    
    public void completar() {
        this.completada = true;
    }
    
    public void agregarTiempo(int minutos) {
        this.tiempoRestanteMinutos += minutos;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getDescripcion() {
        return descripcion;
    } 
    
    public int getTiempoRestanteMinutos() {
        return tiempoRestanteMinutos;
    }
    
    public String getHoraFormato() {
        return horaCreacion != null ? horaCreacion.format(HORA_FORMATO) : "";
    }
    
    public boolean isCompletada() {
        return completada;
    }
    
    public LocalTime getHoraCreacion() {
        return horaCreacion;
    }
}