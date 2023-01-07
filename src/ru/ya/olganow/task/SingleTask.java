package ru.ya.olganow.task;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.description.Type;

public class SingleTask extends Task {
    private Status status;
    public SingleTask(int id, String name, Status status) {
        super(id, name);
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
                ", status='" + getStatus() + '\'' +
                "}";
    }

    public static class ToCreate {
        // this is a domain object for creating task without ID)
        private String name;

        public ToCreate(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}
