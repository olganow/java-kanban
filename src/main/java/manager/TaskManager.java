package main.java.manager;

import main.java.task.SingleTask;
import main.java.task.Task;
import main.java.task.EpicTask;
import main.java.task.Subtask;

import java.util.List;
import java.util.Set;

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

    List<Task> getAllSingleTasks();

    List<Task> getAllEpicTasks();

    List<Task> getAllSubtasks();

    List<Subtask> getSubTasksByEpicId(int id);

    Task getTaskById(int id);

    Set<Task> getPrioritizedTasks();

}
