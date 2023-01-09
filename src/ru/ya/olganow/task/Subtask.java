package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

public class Subtask extends Task {
    private TaskStatus taskStatus;
    private TaskType taskType;
    private EpicTask epicTask;
    private final String epicTaskName;

    public Subtask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus, String epicTaskName) {
        super(id, name, description, taskType);
        this.taskStatus = taskStatus;
        this.epicTaskName = epicTaskName;
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

    public String getEpicTaskName() {
        return epicTaskName;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epic name='" + getEpicTaskName() + '\'' +
                "}\n";
    }


}
