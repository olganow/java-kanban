package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, Task> taskById;
    private Map<Integer, Integer> epicSubtaskById;


    public TaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.taskById = new HashMap<>();
        this.epicSubtaskById = new HashMap<>();
    }

    //option 1: how to save object with genereted id in hashmap
    public void saveNewTask(Task singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        singleTask.setTaskStatus(TaskStatus.NEW);
        taskById.put(singleTask.getId(), singleTask);
    }

    public void saveNewSubTask(Subtask task) {
        // 1: generate new id and save it to the task
        task.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        task.setTaskStatus(TaskStatus.NEW);
        taskById.put(task.getId(), task);
        epicSubtaskById.put(task.getId(), task.getEpicID());
        // 3: Epic Status updated
        setEpicStatus(task.getEpicID());
    }


    public void deleteAllTask() {
        taskById.clear();
        epicSubtaskById.clear();
    }

    public void deleteById(int id) {
        if (taskById.containsKey(id)) {
            if (taskById.get(id).getTaskType() == TaskType.EPIC) {
                ArrayList<Integer> tasksForDelete = new ArrayList<>();
                for (Integer task : this.epicSubtaskById.keySet()) {
                    if (epicSubtaskById.get(task) == id) {
                        tasksForDelete.add(task);
                        taskById.remove(task);
                    }
                }
                for (int i = 0; i < tasksForDelete.size(); i++) {
                    epicSubtaskById.remove(tasksForDelete.get(i));
                }
                taskById.remove(id);
                System.out.println("Задача с id =" + id + ", и ее сабтаски удалены");
            } else if (taskById.get(id).getTaskType() == TaskType.SINGLE) {
                taskById.remove(id);
                System.out.println("Задача с id =" + id + " удалена");
            } else if (taskById.get(id).getTaskType() == TaskType.SUBTASK) {
                int idEpic = epicSubtaskById.get(id);
                ArrayList<Integer> tasksForDelete = new ArrayList<>();
                for (Integer task : this.epicSubtaskById.keySet()) {
                    if (epicSubtaskById.get(task) == id) {
                        tasksForDelete.add(task);
                    }
                }
                for (int i = 0; i < tasksForDelete.size(); i++) {
                    epicSubtaskById.remove(tasksForDelete.get(i));
                }
                taskById.remove(id);
                System.out.println("Задача с id =" + id + " удалена");

                //Epic Status updated
                setEpicStatus(idEpic);
            }

        } else {
            System.out.println("Такого id нет ");
        }
    }

    public void update(Task task) {
        taskById.put(task.getId(), task);
        //Epic Status updated
        // 3: Epic Status updated
        setEpicStatus(task.getId());
        if (taskById.containsKey(task)) {
            if (taskById.get(epicSubtaskById.get(task)).getTaskType() == TaskType.EPIC) {
                setEpicStatus(epicSubtaskById.get(task));
            }
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.taskById.values()) {
            tasks.add(task);
        }
        if (tasks.isEmpty()){
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    public void getSubTasksByEpicId(int id) {
        if (taskById.get(id).getTaskType() == TaskType.EPIC) {
            for (Integer task : this.epicSubtaskById.keySet()) {
                if (epicSubtaskById.get(task) == id) {
                    System.out.println("Эпик номер=" + epicSubtaskById.get(task) + "Subtask номер= " + task);
                }
            }
        } else System.out.println("Эпика с таким номером нет");
    }

    // @return null if no task not found
    public Task getTaskById(int id) {
        if (taskById.get(id) == null) {
            throw new IllegalArgumentException();
        }
        return taskById.get(id);
    }

    public void setEpicStatus(int epicId) {
        if (epicSubtaskById.containsValue(epicId)) {
            if (epicSubtaskById.get(epicId) == null) {
                taskById.get(epicId).setTaskStatus(TaskStatus.NEW);
            }else {
                int counterNew = 0;
                int counterDone = 0;
                int counterInProgress = 0;
                for (Integer task : this.epicSubtaskById.keySet()) {
                    if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.NEW) {
                        counterNew++;
                    }
                    if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.DONE) {
                        counterDone++;
                    }
                    if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.IN_PROGRESS) {
                        counterInProgress++;
                    }
                }
                int counter = counterNew + counterDone + counterInProgress;

                if (counterNew == counter) {
                    taskById.get(epicId).setTaskStatus(TaskStatus.NEW);
                } else if (counterDone == counter) {
                    taskById.get(epicId).setTaskStatus(TaskStatus.DONE);
                } else {
                    taskById.get(epicId).setTaskStatus(TaskStatus.IN_PROGRESS);
                }
            }
        } else {
            System.out.println("Нет эпика с таким ID");
        }
    }
}

class TaskIdGenerator {
    private int nextFreedId = 0;

    public int getNextFreedI() {
        return nextFreedId++;
    }
}

