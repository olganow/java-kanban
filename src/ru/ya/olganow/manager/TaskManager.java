package ru.ya.olganow.manager;

import ru.ya.olganow.description.Status;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    // final поле инициализируется единожды при создании объекта
    private final TaskIdGenerator taskIdGenerator;
    private final HashMap<Integer, Task> taskById;

    public TaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.taskById = new HashMap<>();
    }


    //option 2: more safe, not to create SingleTask object without id
    public void saveNewTask(SingleTask.ToCreate singleTaskToCreate) {
        // 1: create New Task
        int nextFreeId = taskIdGenerator.getNextFreedI();
        SingleTask singleTask = new SingleTask(
                nextFreeId,
                singleTaskToCreate.getName(),
                singleTaskToCreate.getDescription(),
                Status.NEW
        );
        // 2: Save Task
        taskById.put(singleTask.getId(), singleTask);
    }

    public void saveNewEpic(EpicTask.ToCreate singleEpicToCreate) {
        // 1: create NewEpic
        int nextFreeId = taskIdGenerator.getNextFreedI();
        EpicTask epicTask = new EpicTask(
                nextFreeId,
                singleEpicToCreate.getName(),
                singleEpicToCreate.getDescription()
        );
        // 2: Save Epic
        taskById.put(epicTask.getId(), epicTask);
    }


    public void saveNewSubtask(Subtask.ToCreate singleSubtaskToCreate) {
        // 1: create NewEpic
        int nextFreeId = taskIdGenerator.getNextFreedI();
        Subtask subtask = new Subtask(
                nextFreeId,
                singleSubtaskToCreate.getName(),
                singleSubtaskToCreate.getDescription(),
                Status.NEW
        );
        // 2: Save Epic
        taskById.put(subtask.getId(), subtask);
    }

    @Deprecated
    //option 1: how to save object with genereted id in hashmap
    public void saveNewTaskWithID(SingleTask singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        taskById.put(singleTask.getId(), singleTask);
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

    // все таски по ID
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




    public static final class TaskIdGenerator {
        private int nextFreedId = 0;

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }

}
