package com.example;

import java.time.LocalDate;
import java.time.ZoneId;

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

    private LocalDate fechaLocal;

    private static final ZoneId CHILE_ZONE = ZoneId.of("America/Santiago");


    public Inmerso(String task) {
        this.task = task;
        this.fechaLocal = LocalDate.now(CHILE_ZONE);
    }   


    public Inmerso(String task, LocalDate fecha) {
        this.task = task;
        this.fechaLocal = fecha != null ? fecha : LocalDate.now(CHILE_ZONE);
    }

    public Inmerso(){

    }

    public LocalDate getFechaLocal() {
        return fechaLocal;
    }

    public void setFechaLocal(LocalDate fechaLocal) {
        this.fechaLocal = fechaLocal;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id; 
    }

    public String getTask() {
        return task;  //podriamos devolver un json o un objeto para trabajarlo... nose si string
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
