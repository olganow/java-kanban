package ru.ya.olganow.task;

import ru.ya.olganow.status.TaskStatus;

public class Task extends TaskItem{
    public Task(int id, String name, String description, TaskStatus taskStatus){
        super(id,  name, description,  taskStatus);
    }

}
