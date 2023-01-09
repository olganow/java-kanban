package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class Subtask extends Task {
    private TaskStatus taskStatus;
    private TaskType taskType;
    private EpicTask epicTask;
    private final int epicID;

    public Subtask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus, int epicID) {
        super(id, name, description, taskType, taskStatus);
        this.epicID = epicID;
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

    public int getEpicID() {
        return epicID;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epic id='" + getEpicID() + '\'' +
                "}\n";
    }


}
