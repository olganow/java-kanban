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
        epicTask.getSubtaskIds().add(id);
        subtaskById.put(id, subtask);
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
        subtaskById.clear();
        for (Integer epicID: epicTaskById.keySet()) {
            epicTaskById.get(epicID).getSubtaskIds().clear();
            setEpicStatus(epicID);
        }
    }

    public void deleteAllTask() {
        deleteAllSingleTask();
        deleteAllEpicTask();
        deleteAllSubtask();
    }

    public void deleteById(int id) {
        if (singleTaskById.containsKey(id)) {
            singleTaskById.remove(id);
            System.out.println("Задача с id=" + id + " удалена");

        } else if (epicTaskById.containsKey(id)) {
            EpicTask epicTask = epicTaskById.remove(id);
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                subtaskById.remove(subtaskId);
            }
            System.out.println("Задача с id=" + id + ", и ее сабтаски удалены");

        } else if (subtaskById.containsKey(id)) {
            Subtask subtask = subtaskById.remove(id);
            int epicId = subtask.getEpicId();
            System.out.println("Подзадача с id=" + id + " удалена");

            //Epic Status and Epic List updated
            epicTaskById.get(epicId).getSubtaskIds().remove((Integer.valueOf(id)));
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
        //Epic Status updated
        setEpicStatus(epicTask.getId());
    }

    public void updateSubtask(Subtask subtask) {
        subtaskById.put(subtask.getId(), subtask);
        //Epic Status updated
        setEpicStatus(subtask.getEpicId());
    }


    public ArrayList<Task> getAllSingleTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.singleTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public ArrayList<Task> getAllEpicTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.epicTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public ArrayList<Task> getAllSubtasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.subtaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public ArrayList<Subtask> getSubTasksByEpicId(int id) {
        if (epicTaskById.containsKey(id)) {
            EpicTask epic = epicTaskById.get(id);
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.add(subtaskById.get(subtaskId));
            }
            return subtasks;
        }else {
            throw new IllegalArgumentException();
        }
    }


    public Task getTaskById(int id) {
        if (singleTaskById.containsKey(id)) {
            return singleTaskById.get(id);
        } else if (epicTaskById.containsKey(id)) {
            return epicTaskById.get(id);
        } else if (subtaskById.containsKey(id)) {
            return subtaskById.get(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

        private void setEpicStatus(int epicId) {
        EpicTask epicTask = epicTaskById.get(epicId);
        if (epicTask.getSubtaskIds().isEmpty()) {
            epicTaskById.get(epicId).setTaskStatus(TaskStatus.NEW);
        } else {
            int counterNew = 0;
            int counterDone = 0;
            EpicTask epic = epicTaskById.get(epicId);

            for (Integer subtaskId : epic.getSubtaskIds()) {
                TaskStatus status = subtaskById.get(subtaskId).getTaskStatus();
                if (status == TaskStatus.NEW) {
                    counterNew++;
                }
                if (status == TaskStatus.DONE) {
                    counterDone++;
                }
            }
            if (counterNew == epic.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.NEW);
            } else if (counterDone == epic.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.DONE);
            } else {
                epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }


//    private void setEpicStatus(int epicId) {
//        EpicTask epicTask = epicTaskById.get(epicId);
//        if (epicTaskById.get(epicId).getSubtaskIds().size() == 0) {
//            epicTaskById.get(epicId).setTaskStatus(TaskStatus.NEW);
//        } else {
//            int counterNew = 0;
//            int counterDone = 0;
//            int counterInProgress = 0;
//            for (Integer task : this.subtaskById.keySet()) {
//                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.NEW) {
//                    counterNew++;
//                }
//                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.DONE) {
//                    counterDone++;
//                }
//                if (subtaskById.get(task).getEpicId() == epicId && subtaskById.get(task).getTaskStatus() == TaskStatus.IN_PROGRESS) {
//                    counterInProgress++;
//                }
//            }
//            int counter = counterNew + counterDone + counterInProgress;
//            if (counterNew == counter) {
//                epicTask.setTaskStatus(TaskStatus.NEW);
//            } else if (counterDone == counter) {
//                epicTask.setTaskStatus(TaskStatus.DONE);
//            } else {
//                epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
//            }
//        }
//    }

    private static class TaskIdGenerator {
        private int nextFreedId = 0;

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }
}
