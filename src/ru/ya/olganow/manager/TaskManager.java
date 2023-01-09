package ru.ya.olganow.manager;

import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager  {
    // final поле инициализируется единожды при создании объекта
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, Task> taskById;
    private HashMap<Integer, String> epicSubtaskById;

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

    public void saveNewSubTask(Subtask subtask) {
        // 1: generate new id and save it to the task
        subtask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        taskById.put(subtask.getId(), subtask);
        epicSubtaskById.put(subtask.getId(), subtask.getEpicTaskName());
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

    //вернуть названия эпи
    public ArrayList<String> getAllSubtasks() {
        ArrayList<String> tasks = new ArrayList<>();
        //add filter if you would like only subtask
        for (String task : this.epicSubtaskById.values()) {
            tasks.add(task);
        }
        return tasks;
    }


    //все таски по ID
    public ArrayList<Task> getTaskByIds(List<Integer> taskIds) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Integer id : taskIds) {
            tasks.add(this.taskById.get(id));
        }
        return tasks;
    }



    // @return null if no task not found
    public Task getTaskById(int id) {
        return taskById.get(id);
    }

    //
//
//
    public static final class TaskIdGenerator {
        private int nextFreedId = 0;

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }

}
