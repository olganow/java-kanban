package ru.ya.olganow.task;

import ru.ya.olganow.status.TaskStatus;

public class Subtask extends TaskItem{
    public Subtask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
    }
}