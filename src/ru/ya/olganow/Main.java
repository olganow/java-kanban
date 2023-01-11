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
        SingleTask singleTask = new SingleTask("Single safe Task", "Desc SST", TaskType.SINGLE);
        taskManager.saveNewTask(singleTask);
        SingleTask singleTask2 = new SingleTask("Another safe Task", "Desc AST", TaskType.SINGLE);
        taskManager.saveNewTask(singleTask2);


        //Two Epic created
        EpicTask epicTask1 = new EpicTask("First epic", "Desc FE", TaskType.EPIC);
        taskManager.saveNewTask(epicTask1);
        Subtask subtask1 = new Subtask( "First subtask", "Desc FSB", TaskType.SUBTASK,  2);
        taskManager.saveNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskType.SUBTASK, 2);
        taskManager.saveNewSubTask(subtask2);

        EpicTask epicTask2 = new EpicTask( "Second epic", "Desc FE", TaskType.EPIC);
        taskManager.saveNewTask(epicTask2);
        Subtask subtask3 = new Subtask( "Third subtask", "Desc FSB", TaskType.SUBTASK, 5);
        taskManager.saveNewSubTask(subtask3);

        // Get a list with all tasks
        System.out.println("Получить список всех задач\n" + taskManager.getAllTasks());

        //Delete by ID
        taskManager.deleteById(6);

        //Get all tasks by Id
        System.out.println("Получить по ID\n" + taskManager.getTaskById(3));

        //Get all subtasks by EpicId
        System.out.println("Получить список все подзадач:");
        taskManager.getSubTasksByEpicId(2);

        //Update task
        singleTask2 = new SingleTask(1,"Another safe Task--", "Desc AST", TaskType.SINGLE, TaskStatus.IN_PROGRESS);
        taskManager.update(singleTask2);

        subtask3 = new Subtask( 6,"------", "Desc FSB", TaskType.SUBTASK, TaskStatus.IN_PROGRESS, 2);
        taskManager.update(subtask3);

        epicTask2 = new EpicTask(5, "Second epic--", "Desc FE", TaskType.EPIC);
        taskManager.update(epicTask2);

        //Delete by ID
        taskManager.deleteById(6);

        // Delete all tasks
        taskManager.deleteAllTask();

    }


}
