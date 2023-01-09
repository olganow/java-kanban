package ru.ya.olganow.task;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<Subtask> subtasksList;

    public EpicTask(int id, String name, String description,  TaskType taskType, TaskStatus taskStatus) {
        super(id, name, description, taskType, taskStatus);
        this.subtasksList = new ArrayList<>();
    }

    @Override
    public TaskStatus getTaskStatus() {
        // if all subtasks are new-->Status New
        //todo
        return TaskStatus.NEW;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public List<Subtask> getSubtasks() {
        return subtasksList;
    }

//    @Override
//    public int getId() {
//        return id;
//    }


    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" +getId() +
                ", name='" + getName() +'\'' +
                ", description='" + getDescription() + '\'' +
                "status=" +  getTaskStatus() + "," +
                "subtask=" + subtasksList +
                "}\n";
    }


}
