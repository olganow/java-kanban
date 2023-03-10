package main.java.task;

import main.java.description.TaskType;
import main.java.description.TaskStatus;

import java.time.Instant;

public class SingleTask extends Task {

    public SingleTask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public SingleTask(String name, String description, TaskStatus taskStatus, Instant startTime, Instant endTime) {
        super(name, description, taskStatus, startTime, endTime);
    }
    public SingleTask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }

    public SingleTask(int id, String name, String description, TaskStatus taskStatus, Instant startTime,Instant endTime) {
        super(id, name, description, taskStatus, startTime, endTime);
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
                ", end time ='" + getEndTime() + '\'' +
                "}\n";
    }

}
