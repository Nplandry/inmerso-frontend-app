package com.example;

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

    public Inmerso(String task){
        this.task = task;
    }

    public Inmerso(){

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
