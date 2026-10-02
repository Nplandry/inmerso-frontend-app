package com.example;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity 
public class Inmerso {
    @Id 
    @GeneratedValue 
    private Long id;

    private String task;

    private boolean done;

    private LocalTime fechaLocal;

    private int tiempoRestante;

    private static final ZoneId CHILE_ZONE = ZoneId.of("America/Santiago");
    private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");


    public Inmerso(String task) {
        this.task = task;
        this.fechaLocal = LocalTime.now(CHILE_ZONE);
        this.tiempoRestante = 60;
    }   

    public Inmerso(){

    }

    public int getTiempoRestante(){
        return tiempoRestante;
    }

    public void sumarTiempoRestante(){
        this.tiempoRestante += 15;
    }

    public LocalTime getFechaLocal() {
        return fechaLocal;
    }

    public String getHoraFormato() {
        return fechaLocal != null ? fechaLocal.format(HORA_FORMATO) : "";
    }

    public void setFechaLocal(LocalTime fechaLocal) {
        this.fechaLocal = fechaLocal;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id; 
    }
    

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}