package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    // final поле инициализируется единожды при создании объекта
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
        taskById.put(singleTask.getId(), singleTask);
    }

    public void saveNewSubTask(Subtask task) {
        // 1: generate new id and save it to the task
        task.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        taskById.put(task.getId(), task);
        epicSubtaskById.put(task.getId(), task.getEpicID());
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
            }
        } else {
            System.out.println("Такого id нет ");
        }
    }

    public void update(Task task) {
        taskById.put(task.getId(), task);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        //add filter if you would like only subtask
        for (Task task : this.taskById.values()) {
            tasks.add(task);
        }
        return tasks;
    }

//    //вернуть названия эпи
//    public ArrayList<Integer> getAllSubtasksIdByEpicID(int id) {
//        ArrayList<Integer> tasks = new ArrayList<>();
//        //add filter if you would like only subtask
//        for (Integer task : this.epicSubtaskById.values()) {
//            tasks.add(task);
//
//        }
//        return tasks;
//    }


    //все таски по ID
    public ArrayList<Task> getTaskByIds(List<Integer> taskIds) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Integer id : taskIds) {
            tasks.add(this.taskById.get(id));
        }
        return tasks;
    }

    public void getSubTasksById(int id) {
        for (Integer task : this.epicSubtaskById.keySet()) {
            if (epicSubtaskById.get(task) == id) {
                System.out.println("Эпик номер= ='" + epicSubtaskById.get(task) + "Subtask номер= " + task);
            }
        }
    }


    // @return null if no task not found
    public Task getTaskById(int id) {
        if (taskById.get(id) == null) {
            throw new IllegalArgumentException();
        }
        return taskById.get(id);
    }
}

class TaskIdGenerator {
    private int nextFreedId = 1;

    public int getNextFreedI() {
        return nextFreedId++;
    }
}


