package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class Subtask extends Task {
    private TaskStatus taskStatus;
    private TaskType taskType;
    private EpicTask epicTask;
    private final int epicID;

    public Subtask(String name, String description, TaskType taskType,  int epicID){
        super(name, description, taskType);
        this.epicID = epicID;
        this.taskStatus = taskStatus;
    }


//    public Subtask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus, int epicID) {
//        super(id, name, description, taskType, taskStatus);
//        this.epicID = epicID;
//    }
//


    @Override
    public TaskStatus getTaskStatus() {
       // TaskStatus status = super.getTaskStatus();
        return taskStatus;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    public int getEpicID() {
        return epicID;
    }


    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status=" + getTaskStatus() +
                ", epic id='" + getEpicID() + '\'' +
                "}\n";
    }


}
