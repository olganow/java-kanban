package main.java;

import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        // Two single task created
        SingleTask singleTask = new SingleTask("Single safe Task", "Desc SST",
                TaskStatus.NEW, null, 8400L);
        taskManager.addSingleTask(singleTask);
        SingleTask singleTask2 = new SingleTask("Another safe Task", "Desc AST",
                TaskStatus.NEW, Instant.ofEpochMilli(16393035600000L), 7107568400L);
        taskManager.addSingleTask(singleTask2);
        SingleTask singleTask3 = new SingleTask("Another safe Task 3", "Desc AST",
                TaskStatus.NEW, Instant.ofEpochMilli(1035600000L), 1000000L);
        taskManager.addSingleTask(singleTask3);
        SingleTask singleTask4 = new SingleTask("Another safe Task 4", "Desc AST",
                TaskStatus.NEW, null, 8400L);
        taskManager.addSingleTask(singleTask4);
        SingleTask singleTask5 = new SingleTask("Another safe Task 5", "Desc AST",
                TaskStatus.NEW, null, 0L);
        taskManager.addSingleTask(singleTask5);
        SingleTask singleTask6 = new SingleTask("Another safe Task 6", "Desc AST",
                TaskStatus.NEW, null, 56400L);
        taskManager.addSingleTask(singleTask6);


        //Two Epic created
        EpicTask epicTask1 = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask1);
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB",
                TaskStatus.NEW, null, 500, epicTask1.getId());
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB",
                TaskStatus.NEW, null, 500, epicTask1.getId());
        taskManager.addNewSubTask(subtask2);

        EpicTask epicTask2 = new EpicTask("Second epic", "Desc FE");
        taskManager.addEpicTask(epicTask2);
        Subtask subtask3 = new Subtask("Third subtask", "Desc FSB", TaskStatus.DONE,
                Instant.ofEpochMilli(91625714000000L), 8400L, epicTask2.getId());
        taskManager.addNewSubTask(subtask3);

        // Get a list with all tasks
        System.out.println("Получить список всех одиночных задач\n" + taskManager.getAllSingleTasks());
        //    System.out.println("Получить список всех эпиков\n" + taskManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + taskManager.getAllSubtasks());

        // Delete all tasks
//        System.out.println("Все одиночные задачи удалены");
//        taskManager.deleteAllSingleTask();
//        System.out.println("Все эпики удалены");
//        taskManager.deleteAllEpicTask();
//        System.out.println("Все сабтаски удалены");
//        taskManager.deleteAllSubtask();
//        System.out.println("Все задачи удалены");
//        taskManager.deleteAllTask();

        //Delete by ID
//        System.out.println("Задача с указанным id удалена");
//        taskManager.deleteById(3);

        //Get all tasks by Id
   /*     System.out.println("Получить по ID\n" + taskManager.getTaskById(0));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(1));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(2));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(3));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(2));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(5));*/
//
//        //Get all subtasks by EpicId
//        System.out.println("Получить сабтаски по ID эпика:" + taskManager.getSubTasksByEpicId(2));
//
        //Get search history
        System.out.println("Получить историю поиска:\n" + taskManager.getHistory());
//
//        //Update task
//        singleTask2 = new SingleTask(1, "Another safe Task--", "Desc AST", TaskStatus.IN_PROGRESS);
//        taskManager.updateSingleTask(singleTask2);
//
//        subtask3 = new Subtask(3, "Subtask--", "Desc FSB", TaskStatus.NEW, 2);
//        taskManager.updateSubtask(subtask3);
//
//        epicTask2 = new EpicTask(5, "Second epic--", "Desc FE", epicTask2.getSubtaskIds());
//        taskManager.updateEpicTask(epicTask2);

        System.out.println("Получить отсортированные по времени начала задачи и сабтаски:\n" +
                taskManager.getPrioritizedTasks());
    }
}
