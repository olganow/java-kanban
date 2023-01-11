package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class SingleTask extends Task {
    private TaskStatus taskStatus;
    private int id;

    public SingleTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }

     public SingleTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(id, name, description, taskType);
        this.id=id;
        this.taskStatus=taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }


    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SINGLE;
    }

    @Override
    public String toString(){
        return  "SingleTask{" +
                "id=" + getId() +
                ", name='" + getName() +'\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status='" + getTaskStatus() + '\'' +
                "}\n";
    }

}
