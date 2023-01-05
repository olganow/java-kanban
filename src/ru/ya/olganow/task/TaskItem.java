package ru.ya.olganow.task;

import ru.ya.olganow.status.TaskStatus;

public class TaskItem {
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus;

    public TaskItem(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public TaskItem(int id, String name, String description, TaskStatus taskStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
