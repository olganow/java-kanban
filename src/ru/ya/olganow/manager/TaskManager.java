package ru.ya.olganow.manager;

import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;

public interface TaskManager {

    //option 1: how to save object with generated id in hashmap
    void saveSingleTask(SingleTask singleTask);

    void saveEpicTask(EpicTask epicTask);

    void saveNewSubTask(Subtask subtask);

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

}
