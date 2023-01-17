package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class Subtask extends Task {
    private final int epicId;
    TaskType taskType;
    public Subtask(String name, String description, TaskType taskType, TaskStatus taskStatus, int epicId) {
        super(name, description,taskStatus);
        this.epicId = epicId;
        this.taskType=taskType;
    }

    public Subtask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus, int epicId) {
        super(id, name, description,  taskStatus);
        this.epicId = epicId;
        this.taskType=taskType;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status=" + getTaskStatus() +
                ", epic id='" + getEpicId() + '\'' +
                "}\n";
    }


}
