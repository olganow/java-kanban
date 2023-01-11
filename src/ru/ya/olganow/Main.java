package ru.ya.olganow;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.manager.TaskManager;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;


public class Main {

    public static void main(String[] args) {
        //   System.out.println("Поехали!");

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

        // Delete all tasks
        // taskManager.deleteAllTask();

        //Get all tasks by Id
        System.out.println("Получить по ID\n" + taskManager.getTaskById(3));

        //Get all subtasks by EpicId
        System.out.println("Получить список все подзадач:");
        taskManager.getSubTasksByEpicId(1);


        //Update task
        SingleTask singleTask3 = new SingleTask("Single safe Task Changed", "Desc SST", TaskType.SINGLE);
        taskManager.update(singleTask3);
        System.out.println("Получить по ID измененную задачу\n" + taskManager.getTaskById(1));

        //Delete by ID
      //  taskManager.deleteById(4);

        //7 - Получение списка всех подзадач определённого эпика.");
        System.out.println("11111 - Получить список все подзадач\n");
        taskManager.getSubTasksByEpicId(1);


//        // how to change status
//        subtask1.setTaskStatus(TaskStatus.DONE);
//         subtask2.setTaskStatus(TaskStatus.DONE);
//     //  taskManager.update(singleTask);
//        System.out.println("==================");



//        Subtask subtask4 = new Subtask("4thd subtask", "Desc SSB", TaskType.SUBTASK, 2);
//        taskManager.saveNewSubTask(subtask4);



        System.out.println("Получить список всех задач\n" + taskManager.getAllTasks());
    }


}
