package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

public class Subtask extends Task {
    private final Status status;
    private final EpicTask epicTask;
    public Subtask(int id, String name, Status status, EpicTask epicTask) {
        super(id, name);
        this.status = status;
        this.epicTask = epicTask;
    }
    @Override
    public Status getStatus() {
        // if all subtasks are new-->Status New
        return status;
    }

    @Override
    public Type getType(){
        return Type.SUB;
    }

    @Override
    public String toString(){
        return  "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() +'\'' +
                "}";
    }
}