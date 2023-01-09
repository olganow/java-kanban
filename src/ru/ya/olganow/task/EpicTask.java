package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Subtask> subtasksList;
    private TaskStatus taskStatus;

    public EpicTask(int id, String name, String description, TaskType taskType, TaskStatus taskStatus) {
        super(id, name, description, taskType, taskStatus);
        this.subtasksList = new ArrayList<>();
    }

    @Override
    public TaskStatus getTaskStatus() {
        if (subtasksList.isEmpty()) {
            return taskStatus.NEW;
        }
//        else if ()
//todo
//        return taskStatus.DONE;
        else {
            return TaskStatus.IN_PROGRESS;
        }
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
                "status=" + getTaskStatus() + "," +
                "subtask=" + subtasksList +
                "}\n";
    }


}
