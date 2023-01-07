package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

public class SingleTask extends Task {
    private Status status;
    public SingleTask(int id, String name, String description, Status status) {
        super(id, name, description);
        this.status=status;
    }
    @Override
    public Status getStatus() {
        // if all subtasks are new-->Status New
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public String toString(){
        return  "SingleTask{" +
                "id=" + getId() +
                ", name='" + getName() +'\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                "}\n";
    }

    public static class ToCreate {
        // this is a domain object for creating task without ID)
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
