package ru.ya.olganow.manager;

import ru.ya.olganow.status.TaskStatus;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;

public class TaskManager {
    public int id;
    public String name;
    String description;
    private TaskStatus taskStatus;

    public ArrayList<Task> taskArrayList = new ArrayList<>();

    Task task = new Task(id,  name, description, taskStatus);
   // taskArrayList;
}
