package com.example.infraestructure.persistencia;

import java.time.LocalTime;

public class TareaEntity {
    private long id;
    private String descripcion;
    private boolean completada;
    private LocalTime horaCreacion;
    private int tiempoRestanteMinutos;

    public TareaEntity() {}

    public TareaEntity(String descripcion, LocalTime horaCreacion, int tiempoRestanteMinutos) {
        this.descripcion = descripcion;
        this.horaCreacion = horaCreacion;
        this.tiempoRestanteMinutos = tiempoRestanteMinutos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public LocalTime getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(LocalTime horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public int getTiempoRestanteMinutos() {
        return tiempoRestanteMinutos;
    }

    public void setTiempoRestanteMinutos(int tiempoRestanteMinutos) {
        this.tiempoRestanteMinutos = tiempoRestanteMinutos;
    }
}