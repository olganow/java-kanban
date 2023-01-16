package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, SingleTask> singleTaskById;
    private HashMap<Integer, EpicTask> epicTaskById;
    private HashMap<Integer, Subtask> subtaskById;

    public TaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTaskById = new HashMap<>();
        this.epicTaskById = new HashMap<>();
        this.subtaskById = new HashMap<>();

    }

    //option 1: how to save object with generated id in hashmap
    public void saveSingleTask(SingleTask singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    public void saveEpicTask(EpicTask epicTask) {
        // 1: generate new id and save it to the task
        epicTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        epicTaskById.put(epicTask.getId(), epicTask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
    }

    public void saveNewSubTask(Subtask subtask) {
        // 1: generate new id and save it to the task
        subtask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        EpicTask epicTask = epicTaskById.get(subtask.getEpicId());
        Integer id = subtask.getId();
        epicTask.getSubtaskList().add(id);
        subtaskById.put(subtask.getId(), subtask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
    }


    public void deleteAllSingleTask() {
        singleTaskById.clear();
    }

    public void deleteAllEpicTask() {
        epicTaskById.clear();
        subtaskById.clear();
    }

    public void deleteAllSubtask() {
        ArrayList<Integer> tasksForUpdate = new ArrayList<>();
        for (Integer task : this.subtaskById.keySet()) {
            tasksForUpdate.add(subtaskById.get(task).getEpicId());

            System.out.println(subtaskById.get(task).getEpicId());
            setEpicStatus((subtaskById.get(task).getEpicId()));
            EpicTask epicTask = epicTaskById.get((subtaskById.get(task).getEpicId()));
            epicTask.getSubtaskList().clear();
        }
        subtaskById.clear();
    }

    public void deleteAllTask() {
        deleteAllSingleTask();
        deleteAllEpicTask();
        deleteAllSubtask();
    }

    public void deleteById(int id) {
        if (singleTaskById.containsKey(id)) {
            singleTaskById.remove(id);
            System.out.println("Задача с id =" + id + " удалена");
        } else if (epicTaskById.containsKey(id)) {
            if (epicTaskById.get(id).getSubtaskList().isEmpty()) {
                epicTaskById.remove(id);
            } else {
                ArrayList<Integer> tasksForDelete = new ArrayList<>();
                for (Integer task : this.subtaskById.keySet()) {
                    if (subtaskById.get(task).getEpicId() == id) {
                        System.out.println("====");
                        tasksForDelete.add(task);
                    }
                }
                for (int i = 0; i < tasksForDelete.size(); i++) {
                    subtaskById.remove(tasksForDelete.get(i));
                }
                epicTaskById.remove(id);
            }
            System.out.println("Задача с id =" + id + ", и ее сабтаски удалены");

        } else if (subtaskById.containsKey(id)) {
            int epicId = subtaskById.get(id).getEpicId();
            subtaskById.remove(id);
            System.out.println("Задача с id =" + id + " удалена");
            //Epic Status updated
            setEpicStatus(epicId);
        } else {
            System.out.println("Такого id нет ");
        }
    }


    public void updateSingleTask(SingleTask singleTask) {
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    public void updateEpicTask(EpicTask epicTask) {
        epicTaskById.put(epicTask.getId(), epicTask);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskById.put(subtask.getId(), subtask);
        //Epic Status updated
        setEpicStatus(subtask.getEpicId());
    }


    public ArrayList<Task> getAllSingleTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.singleTaskById.values()) {
            tasks.add(task);
        }
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public ArrayList<Task> getAllEpicTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.epicTaskById.values()) {
            tasks.add(task);
        }
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public ArrayList<Task> getAllSubtasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.subtaskById.values()) {
            tasks.add(task);
        }
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public void getSubTasksByEpicId(int id) {
        if (epicTaskById.containsKey(id)) {
            for (Integer task : this.subtaskById.keySet()) {
                if (subtaskById.get(task).getEpicId() == id) {
                    System.out.println("Эпик номер=" + id + ", Subtask номер=" + task);
                }
            }
        } else {
            System.out.println("Эпика с номером " + id + " нет");
        }
    }

    //@return null if no task not found
    public Task getTaskById(int id) {
        if (!singleTaskById.containsKey(id) && !epicTaskById.containsKey(id) && !subtaskById.containsKey(id)) {
            throw new IllegalArgumentException();
        } else {
            if (singleTaskById.containsKey(id)) {
                return singleTaskById.get(id);
            } else if (epicTaskById.containsKey(id)) {
                return epicTaskById.get(id);
            } else return subtaskById.get(id);
        }
    }

    private void setEpicStatus(int epicId) {
        EpicTask epicTask = epicTaskById.get(epicId);
        if (epicTaskById.get(epicId).getSubtaskList().size() == 0) {
            epicTaskById.get(epicId).setTaskStatus(TaskStatus.NEW);
        } else {
            int counterNew = 0;
            int counterDone = 0;
            int counterInProgress = 0;
            for (Integer task : this.subtaskById.keySet()) {
                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.NEW) {
                    counterNew++;
                }
                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.DONE) {
                    counterDone++;
                }
                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.IN_PROGRESS) {
                    counterInProgress++;
                }
            }
            int counter = counterNew + counterDone + counterInProgress;
            if (counterNew == counter) {
                epicTask.setTaskStatus(TaskStatus.NEW);
            } else if (counterDone == counter) {
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
