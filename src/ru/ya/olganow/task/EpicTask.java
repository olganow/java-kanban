package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Subtask> subtasks;

    public EpicTask(int id, String name, String description) {
        super(id, name, description);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
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
                "id=" +getId() +
                ", name='" + getName() +'\'' +
                ", description='" + getDescription() + '\'' +
                "status=" + getStatus() + "," +
                "subtask=" + subtasks +
                "}\n";
    }

    public static class ToCreate {
        // this is a domain object for creating epic without ID)
        private String name;
        private String description;

        public ToCreate(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
