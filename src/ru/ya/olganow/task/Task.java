package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

public abstract class Task {
    private  int id;
    private final String name;


    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {//int?
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {//Inteder?
        this.id = id;
    }

    public abstract Status getStatus();
    public abstract Type getType();

    @Override
    public String toString(){
        return  "Task{" +
                "id=" + id +
                ", name='" + name +'\'' +
                "}";

    }

}
