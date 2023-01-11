package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class SingleTask extends Task {
    private TaskStatus taskStatus;
    private int id;

    public SingleTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
        this.taskStatus=taskStatus;
        this.id=id;
    }

     public SingleTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(name, description, taskType);
        this.id=id;

    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }


    @Override
    public TaskStatus getTaskStatus() {
        // if all subtasks are new-->Status New
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
