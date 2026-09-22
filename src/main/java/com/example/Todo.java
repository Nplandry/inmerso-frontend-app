package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity 
public class Todo {
    @Id 
    @GeneratedValue 
    private long id;

    private String task;

    private boolean done;

    public Todo(String task){
        this.task = task;
    }


    public Todo(){

    }

    public long getId(){
        return id;
    }

    public void setId(long id){
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
