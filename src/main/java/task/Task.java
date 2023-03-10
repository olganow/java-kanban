package main.java.task;

import main.java.description.TaskType;
import main.java.description.TaskStatus;

import java.time.Instant;

public abstract class Task {
    //Task is abstract class and cannot be instantiated
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus = TaskStatus.NEW;
   long duration;
    Instant startTime;
    Instant endTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String name, String description, TaskStatus taskStatus, Instant startTime, Instant endTime) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description, Instant startTime, Instant endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus, Instant startTime, Instant endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.endTime = endTime;
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
        if (duration != 0)
            return duration;
        else return 0;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        if (startTime != null){
        return startTime;
        } else return Instant.MIN;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
       if (startTime != null  && duration != 0){
            return startTime.plusSeconds(duration);
       } else return Instant.now();

    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}";

    }

}