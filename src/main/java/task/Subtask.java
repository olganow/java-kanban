package main.java.task;

import main.java.description.TaskStatus;
import main.java.description.TaskType;

import java.time.Instant;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, int epicId) {
        super(id, name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, Instant startTime, Instant endTime, int epicId) {
        super(id, name, description, taskStatus,startTime, endTime);
        this.epicId = epicId;
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
                ", start time ='" + getStartTime() + '\'' +
                ", end time ='" + getEndTime() + '\'' +
                ", epic id='" + getEpicId() + '\'' +
                "}\n";
    }


}
