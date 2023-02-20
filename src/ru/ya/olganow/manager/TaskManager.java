package ru.ya.olganow.manager;

import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();
    void addSingleTask(SingleTask singleTask);

    void addEpicTask(EpicTask epicTask);

    void addNewSubTask(Subtask subtask);

    void deleteAllSingleTask();

    void deleteAllEpicTask();

    void deleteAllSubtask();

    void deleteAllTask();

    void deleteById(int id);

    void updateSingleTask(SingleTask singleTask);

    void updateEpicTask(EpicTask epicTask);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getAllSingleTasks();

    ArrayList<Task> getAllEpicTasks();

    ArrayList<Task> getAllSubtasks();

    ArrayList<Subtask> getSubTasksByEpicId(int id);

    Task getTaskById(int id);
    void save();

}
