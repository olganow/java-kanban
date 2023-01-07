package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private final List<Subtask> subtasks;

    public EpicTask(int id, String name) {
        super(id, name);
        this.subtasks = new ArrayList<>();
    }

    @Override
    public Status getStatus() {
 // if all subtasks are new-->Status New
        //todo
        return Status.NEW;
    }

    @Override
    public Type getType(){
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" +getId() + "," +
                "sttus=" + getStatus() + "," +
                "subtask=" + subtasks +
                "}";
    }


}
