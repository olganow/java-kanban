package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager  {
    // final поле инициализируется единожды при создании объекта
    private final TaskIdGenerator taskIdGenerator;
    private HashMap<Integer, Task> taskById;
    private Map<Integer, List<Integer>> epicSubtaskById;


    public TaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.taskById = new HashMap<>();
        this.epicSubtaskById = new HashMap<Integer, List<Integer>>();
    }

    //option 1: how to save object with genereted id in hashmap
    public void saveNewTask(Task singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        taskById.put(singleTask.getId(), singleTask);
    }

    public void saveNewSubTask(Task task) {
        // 1: generate new id and save it to the task
        task.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        taskById.put(task.getId(), task);

        List<Integer> tasks = epicSubtaskById.get(task.getTaskType());
        if (tasks == null){
            tasks = new ArrayList<>();
            tasks.add(task.getId());
            epicSubtaskById.put(task.getId(), tasks);
        }
        else {
            tasks.add(task.getId());
        }


      //  taskTypes.put(subtask.getId(), subtask.getEpicID());
    }


public void deleteAllTask(){
    taskById.clear();
    epicSubtaskById.clear();
}

    public void deleteById(int id){
        if (taskById.containsKey(id)) {
            System.out.println("taskById="+ taskById.get(id));
            if (taskById.get(id).getTaskType()== TaskType.EPIC){
                for (List<Integer> epic : epicSubtaskById.values()) {
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

    public  ArrayList<Task> getAllTasks() {
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
