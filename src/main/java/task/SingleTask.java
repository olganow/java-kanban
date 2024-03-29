package main.java.task;

import main.java.description.TaskType;
import main.java.description.TaskStatus;

import java.time.Instant;

public class SingleTask extends Task {

    public SingleTask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public SingleTask(String name, String description, TaskStatus taskStatus, Instant startTime, long duration) {
        super(name, description, taskStatus, startTime, duration);
    }

    public SingleTask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }

    public SingleTask(int id, String name, String description, TaskStatus taskStatus, Instant startTime, long duration) {
        super(id, name, description, taskStatus, startTime, duration);
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
                ", start time ='" + getStartTime() + '\'' +
                ", duration ='" + getDuration() + '\'' +
                "}\n";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
