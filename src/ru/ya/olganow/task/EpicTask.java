package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Subtask> subtasksList;
    private TaskStatus taskStatus;

    public EpicTask(String name, String description, TaskType taskType) {
        super(name, description, taskType);
        this.taskStatus = taskStatus;
        this.subtasksList = new ArrayList<>();
    }
//    public EpicTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
//        super(id, name, description, taskType, taskStatus);
//        this.subtasksList = new ArrayList<>();
//    }

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
        return TaskType.EPIC;
    }

    public List<Subtask> getSubtasks() {
        return subtasksList;
    }

    public void add(Subtask subtask) {
        subtasksList.add(subtask);
    }

    public void remove(Subtask subtask) {
        subtasksList.remove(subtask);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", type='" + getTaskType() + '\'' +
                ", status=" + getTaskStatus() +
                "}\n";
    }


}
