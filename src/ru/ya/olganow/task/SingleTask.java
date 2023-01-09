package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class SingleTask extends Task {
    private TaskStatus taskStatus;

    public SingleTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(id, name, description, taskType);
        this.taskStatus=taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
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
    public String toString(){
        return  "SingleTask{" +
                "id=" + getId() +
                ", name='" + getName() +'\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getTaskStatus() + '\'' +
                "}\n";
    }

}
