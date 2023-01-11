package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

import java.util.List;

public class EpicTask extends Task {
    private TaskStatus taskStatus;

    public EpicTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }

    public EpicTask(int id, String name, String description, TaskType taskType) {
        super(id, name, description, taskType);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
    @Override
        public TaskStatus getTaskStatus() {
            return taskStatus;
        }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status=" + getTaskStatus() +
                "}\n";
    }


}
