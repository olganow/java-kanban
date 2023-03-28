package main.java.task;

import main.java.description.TaskStatus;
import main.java.description.TaskType;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus taskStatus, Instant startTime, long duration, int epicId) {
        super(name, description, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, int epicId) {
        super(id, name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus taskStatus, Instant startTime, long duration, int epicId) {
        super(id, name, description, taskStatus, startTime, duration);
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
                ", duration ='" + getDuration() + '\'' +
                ", epic id='" + getEpicId() + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
       return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
