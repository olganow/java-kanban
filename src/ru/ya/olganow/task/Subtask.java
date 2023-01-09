package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class Subtask extends Task {
    private TaskStatus taskStatus;
    private TaskType taskType;
    private EpicTask epicTask;

    public Subtask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description);
        this.taskStatus = taskStatus;


    }

//    public Subtask(int id, String name, String description, TaskStatus taskStatus, EpicTask epicTask) {
//        super(id, name, description);
//        this.taskStatus = taskStatus;
//        this.epicTask = epicTask;
//
//    }


    public EpicTask getEpicTask() {
        return epicTask;
    }

    @Override
    public TaskStatus getTaskStatus() {
        // if all subtasks are new-->Status New
        return taskStatus;
    }

    @Override
    public TaskType getTaskType() {
        return null;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                "}\n";
    }


}
