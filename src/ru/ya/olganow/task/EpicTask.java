package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask(int id, String name, String description) {
        super(id, name, description);
        this.subtaskIds = new ArrayList<>();
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
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
