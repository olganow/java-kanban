package ru.ya.olganow;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.manager.TaskManager;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        // Two single task created
        SingleTask singleTask = new SingleTask("Single safe Task", "Desc SST", TaskType.SINGLE, TaskStatus.NEW);
        taskManager.saveSingleTask(singleTask);
        SingleTask singleTask2 = new SingleTask("Another safe Task", "Desc AST", TaskType.SINGLE,TaskStatus.NEW);
        taskManager.saveSingleTask(singleTask2);

        //Two Epic created
        EpicTask epicTask1 = new EpicTask("First epic", "Desc FE", TaskType.EPIC);
        taskManager.saveEpicTask(epicTask1);
        Subtask subtask1 = new Subtask( "First subtask", "Desc FSB", TaskType.SUBTASK,TaskStatus.NEW,  2);
        taskManager.saveNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskType.SUBTASK,TaskStatus.NEW, 2);
        taskManager.saveNewSubTask(subtask2);

        EpicTask epicTask2 = new EpicTask( "Second epic", "Desc FE", TaskType.EPIC);
        taskManager.saveEpicTask(epicTask2);
        Subtask subtask3 = new Subtask( "Third subtask", "Desc FSB", TaskType.SUBTASK,TaskStatus.NEW, 5);
        taskManager.saveNewSubTask(subtask3);

        // Get a list with all tasks
        System.out.println("Получить список всех одиночных задач\n" + taskManager.getAllSingleTasks());
        System.out.println("Получить список всех эпиков\n" + taskManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + taskManager.getAllSubtasks());

        // Delete all tasks
        //taskManager.deleteAllSingleTask();
        //taskManager.deleteAllEpicTask();
        //taskManager.deleteAllSubtask();
        //taskManager.deleteAllTask();

        //Delete by ID
        //taskManager.deleteById(6);

        //Get all tasks by Id
        System.out.println("Получить по ID\n" + taskManager.getTaskById(1));

        //Get all subtasks by EpicId
        System.out.println("Получить сабтаски по ID эпика:");
        taskManager.getSubTasksByEpicId(2);

        //Update task
        singleTask2 = new SingleTask(1,"Another safe Task--", "Desc AST", TaskType.SINGLE, TaskStatus.IN_PROGRESS);
        taskManager.updateSingleTask(singleTask2);

        subtask3 = new Subtask( 3,"Subtask--", "Desc FSB", TaskType.SUBTASK, TaskStatus.IN_PROGRESS, 2);
        taskManager.updateSubtask(subtask3);

        epicTask2 = new EpicTask(5, "Second epic--", "Desc FE", TaskType.EPIC);
        taskManager.updateEpicTask(epicTask2);
    }

}
