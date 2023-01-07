package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

public class Subtask extends Task {
    private Status status;
    private EpicTask epicTask;

//    public Subtask(int id, String name, String description,  Status status, EpicTask epicTask) {
//        super(id, name,description);
//        this.status = status;
//        this.epicTask = epicTask;
//    }

    public Subtask(int id, String name, String description,  Status status) {
        super(id, name,description);
        this.status = status;
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
                ", description='" + getDescription() + '\'' +
                "}\n";
    }

    public static class ToCreate {
        // this is a domain object for creating subtask without ID)
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