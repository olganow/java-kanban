package ru.ya.olganow.task;

import ru.ya.olganow.status.TaskStatus;

public class Epic extends TaskItem{
    public Epic(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }
}
