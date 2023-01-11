package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskStatus;
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
        for (Task task : this.taskById.values()) {
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

    public void setEpicStatus(int epicId) {
        Task epicTask = taskById.get(epicId);
        if (epicSubtaskById.containsValue(epicId)) {
            int counterNew = 0;
            int counterDone = 0;
            int counterInProgress = 0;
            for (Integer task : this.epicSubtaskById.keySet()) {
                if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.NEW) {
                    counterNew++;
                    System.out.println("counterNew= " + counterNew);
                    System.out.println("111NEW-Эпик номер= ='" + epicSubtaskById.get(task) + "\n" + "Subtask номер= " + task);
                }
                if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.DONE) {
                    counterDone++;
                    System.out.println("counterDone = " + counterDone);
                    System.out.println("111Done-Эпик номер= ='" + epicSubtaskById.get(task) + "\n" + "Subtask номер= " + task);
                }
                if (epicSubtaskById.get(task) == epicId && taskById.get(task).getTaskStatus() == TaskStatus.IN_PROGRESS) {
                    counterInProgress++;
                    System.out.println("ounterInProgress= " + counterInProgress);
                    System.out.println("111pROGRES-Эпик номер= ='" + epicSubtaskById.get(task) + "\n" + "Subtask номер= " + task);
                }
            }
                int counter = counterNew + counterDone + counterInProgress;
                System.out.println("counter Итого ==" + counter + "/n" +
                        "counterNew= " + counterNew + "," + "counterDone =" + counterDone + "," + "counterInProgresss=" + counterInProgress);

                if (counterNew == counter) {
                    taskById.get(epicId).setTaskStatus(TaskStatus.NEW);
                    System.out.println("happy in NEW");
                } else if (counterDone == counter) {
                    taskById.get(epicId).setTaskStatus(TaskStatus.DONE);
                    System.out.println("happy in Done");
                } else {
                    taskById.get(epicId).setTaskStatus(TaskStatus.IN_PROGRESS);
                    System.out.println("happy in progress");
                }




        }


    }
}




class TaskIdGenerator {
    private int nextFreedId = 0;

    public int getNextFreedI() {
        return nextFreedId++;
    }
}


//           System.out.println("111-Эпик номер= ='" + epicSubtaskById.get(task) + "\n" + "Subtask номер= " + task + "\n" +
//                   " taskById.get(epicId)== >'" + taskById.get(epicId) + "\n"
//                   +
//                   "taskById.get(epicId)getTaskStatus()->" + taskById.get(epicId).getTaskStatus() + "\n" +
//                   " epicSubtaskById.containsValue(epicId)== >'" + epicSubtaskById.containsValue(epicId) + "\n" +
//                   "taskById.get(epicId)=>" + taskById.get(epicId));
//                   System.out.println("--");

