package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager  {
    // final поле инициализируется единожды при создании объекта
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, Task> taskById;
    private HashMap<Integer, Integer> epicSubtaskById;


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
        epicSubtaskById.put(subtask.getId(), subtask.getEpicID());
    }


public void deleteAllTask(){
    taskById.clear();
    epicSubtaskById.clear();
}

    public void deleteById(int id){
        if (taskById.containsKey(id)) {
            System.out.println("taskById="+ taskById.get(id));
            if (taskById.get(id).getTaskType()== TaskType.EPIC){
                for (Integer epic : epicSubtaskById.values()) {
                    if (epicSubtaskById.equals(id)) {
                        epicSubtaskById.remove(id);
                        System.out.println("Сабтаски эпика удалены");
                    }
                }
            }
            taskById.remove(id);
            System.out.println("Задача с id ="+ id +" удалена");
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

    //вернуть названия эпи
    public ArrayList<Integer> getAllSubtasksIdByEpicID(int id) {
        ArrayList<Integer> tasks = new ArrayList<>();
        //add filter if you would like only subtask
        for (Integer task : this.epicSubtaskById.keySet()) {
            if (epicSubtaskById.containsValue(id)) {
                System.out.println(epicSubtaskById.values());
                tasks.add(task);
            }
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
        if (taskById.get(id) == null){
            throw new IllegalArgumentException();
        }
        return taskById.get(id);
    }

    public static final class TaskIdGenerator {
        private int nextFreedId = 1;

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }

}
