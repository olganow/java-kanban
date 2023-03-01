package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class SingleTask extends Task {

    public SingleTask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public SingleTask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SINGLE;
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status='" + getTaskStatus() + '\'' +
                "}\n";
    }

}
