package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, SingleTask> singleTaskById;
    private HashMap<Integer, EpicTask> epicTaskById;
    private HashMap<Integer, Subtask> subtaskById;
    private Managers managers;
    private final HistoryManager historyManager;
    public InMemoryTaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTaskById = new HashMap<>();
        this.epicTaskById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.historyManager = managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
      return historyManager.getHistory();
    }

    @Override
    //option 1: how to save object with generated id in hashmap
    public void saveSingleTask(SingleTask singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        // 1: generate new id and save it to the task
        epicTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        epicTaskById.put(epicTask.getId(), epicTask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
    }

    @Override
    public void saveNewSubTask(Subtask subtask) {
        // 1: generate new id and save it to the task
        subtask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        EpicTask epicTask = epicTaskById.get(subtask.getEpicId());
        Integer id = subtask.getId();
        epicTask.getSubtaskIds().add(id);
        subtaskById.put(id, subtask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
    }

    @Override
    public void deleteAllSingleTask() {
        singleTaskById.clear();
    }

    public void deleteAllEpicTask() {
        epicTaskById.clear();
        subtaskById.clear();
    }

    @Override
    public void deleteAllSubtask() {
        subtaskById.clear();
        for (Integer epicId : epicTaskById.keySet()) {
            epicTaskById.get(epicId).getSubtaskIds().clear();
            setEpicStatus(epicId);
        }
    }

    @Override
    public void deleteAllTask() {
        deleteAllSingleTask();
        deleteAllEpicTask();
        deleteAllSubtask();
    }

    @Override
    public void deleteById(int id) {
        if (singleTaskById.containsKey(id)) {
            singleTaskById.remove(id);
            System.out.println("Задача с id=" + id + " удалена");
            historyManager.remove(id);

        } else if (epicTaskById.containsKey(id)) {
            EpicTask epicTask = epicTaskById.remove(id);
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                subtaskById.remove(subtaskId);
            }
            historyManager.remove(id);
            System.out.println("Задача с id=" + id + ", и ее сабтаски удалены");

        } else if (subtaskById.containsKey(id)) {
            Subtask subtask = subtaskById.remove(id);
            int epicId = subtask.getEpicId();
            historyManager.remove(id);
            System.out.println("Подзадача с id=" + id + " удалена");

            //Epic Status and Epic List updated
            epicTaskById.get(epicId).getSubtaskIds().remove((Integer.valueOf(id)));
            setEpicStatus(epicId);
        } else {
            System.out.println("Такого id нет ");
        }
    }

    @Override
    public void updateSingleTask(SingleTask singleTask) {
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTaskById.put(epicTask.getId(), epicTask);
        //Epic Status updated
        setEpicStatus(epicTask.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskById.put(subtask.getId(), subtask);
        //Epic Status updated
        setEpicStatus(subtask.getEpicId());
    }

    @Override
    public ArrayList<Task> getAllSingleTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.singleTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    @Override
    public ArrayList<Task> getAllEpicTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.epicTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    @Override
    public ArrayList<Task> getAllSubtasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.subtaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    @Override
    public ArrayList<Subtask> getSubTasksByEpicId(int id) {
        if (epicTaskById.containsKey(id)) {
            EpicTask epic = epicTaskById.get(id);
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.add(subtaskById.get(subtaskId));
            }
            return subtasks;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (singleTaskById.containsKey(id)) {
            historyManager.add(singleTaskById.get(id));///////
            return singleTaskById.get(id);
        } else if (epicTaskById.containsKey(id)) {
            historyManager.add(epicTaskById.get(id));
            return epicTaskById.get(id);
        } else if (subtaskById.containsKey(id)) {
            historyManager.add(subtaskById.get(id));
            return subtaskById.get(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void setEpicStatus(int epicId) {
        EpicTask epicTask = epicTaskById.get(epicId);
        if (epicTask.getSubtaskIds().isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else {
            int counterNew = 0;
            int counterDone = 0;

            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                TaskStatus status = subtaskById.get(subtaskId).getTaskStatus();
                if (status == TaskStatus.NEW) {
                    counterNew++;
                } else if (status == TaskStatus.DONE) {
                    counterDone++;
                } else {
                    epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                    return;
                }
            }
            if (counterNew == epicTask.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.NEW);
            } else if (counterDone == epicTask.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.DONE);
            } else {
                epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    private static class TaskIdGenerator {
        private int nextFreedId = 0;

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }
}