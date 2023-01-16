package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;

public class EpicTask extends Task {
    private TaskStatus taskStatus;
    private int subtaskID;

    private ArrayList<Integer> subtaskList = new ArrayList<>();

    public EpicTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
    }

    public EpicTask(int id, String name, String description, TaskType taskType) {
        super(id, name, description, taskType);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setSubtaskList(ArrayList<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public int getSubtaskID() {
        return subtaskID;
    }

    public ArrayList<Integer> getSubtaskList() {
        return subtaskList;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status=" + getTaskStatus() +
                ", subtaskList=" + getSubtaskList() +
                "}\n";
    }


}
