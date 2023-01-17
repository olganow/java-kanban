package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;

public class EpicTask extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public EpicTask(String name, String description, TaskType taskType) {
        super(name, description);
    }

    public EpicTask(int id, String name, String description, TaskType taskType) {
        super(id, name, description);
    }


    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
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
                ", subtaskList=" + getSubtaskIds() +
                "}\n";
    }

}
