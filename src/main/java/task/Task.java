package main.java.task;

import main.java.description.TaskType;
import main.java.description.TaskStatus;

import java.time.Instant;
import java.util.Objects;

public abstract class Task {
    //Task is abstract class and cannot be instantiated
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus = TaskStatus.NEW;
    private long duration;
    private Instant startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String name, String description, TaskStatus taskStatus, Instant startTime, long duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description, Instant startTime, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, Instant startTime, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract TaskType getTaskType();

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        if (duration >= 0) {
            this.duration = duration;
        } else {
            System.out.println("Error! Duration can't be negative");
        }
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
            this.startTime = startTime;
    }

    public Instant getEndTime() {
        if (startTime != null) {
            return startTime.plusSeconds(duration);
        } else return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(task.id, id) && duration == task.duration && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && taskStatus == task.taskStatus
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, taskStatus, duration, startTime);
    }
}