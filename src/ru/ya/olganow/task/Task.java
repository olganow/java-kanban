package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public abstract class Task {
    //Task is abstract class and cannot be instantiated
    private int id;
    private String name;
    private String description;
    private TaskType taskType;

    public Task(int id, String name, String description, TaskType taskType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskType = taskType;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract TaskStatus getTaskStatus();
   // public abstract TaskType getTaskType();

    @Override
    public String toString(){
        return  "Task{" +
                "id=" + id +
                ", name='" + name +'\'' +
                "}";

    }

}