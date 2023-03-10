package main.java.task;

import main.java.description.TaskStatus;
import main.java.description.TaskType;

import java.time.Instant;
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

    public EpicTask(int id, String name, String description, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.subtaskIds = new ArrayList<>();
    }

    public EpicTask(int id, String name, String description, TaskStatus taskStatus, Instant startTime, Instant endTime) {
        super(id, name, description, taskStatus);
        this.subtaskIds = new ArrayList<>();
        this.startTime = startTime;
        this.endTime = endTime;
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
                ", start time ='" + getStartTime() + '\'' +
                ", end time ='" + getEndTime() + '\'' +
                ", subtaskList=" + getSubtaskIds() +
                "}\n";
    }

}
