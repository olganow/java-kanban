package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class SingleTask extends Task {
    TaskType taskType;
    public SingleTask(String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(name, description,taskStatus);
        this.taskType=taskType;
    }

    public SingleTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.taskType=taskType;
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
