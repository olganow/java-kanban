package ru.ya.olganow;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.manager.TaskManager;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

public class Main {

    public static void main(String[] args) {
        //   System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        // Two single task created
        SingleTask singleTask = new SingleTask(0, "Single safe Task", "Desc SST", TaskType.SINGLE, TaskStatus.NEW);
        taskManager.saveNewTask(singleTask);
        SingleTask singleTask2 = new SingleTask(0, "Another safe Task", "Desc AST", TaskType.SINGLE, TaskStatus.NEW);
        taskManager.saveNewTask(singleTask2);


        //Two Epic created
        EpicTask epicTask1 = new EpicTask(0, "First epic", "Desc FE", TaskType.EPIC, TaskStatus.NEW);
        taskManager.saveNewTask(epicTask1);
        Subtask subtask1 = new Subtask(0, "First subtask", "Desc FSB", TaskType.SUBTASK, TaskStatus.NEW, 3);
        taskManager.saveNewSubTask(subtask1);
        Subtask subtask2 = new Subtask(0, "Second subtask", "Desc SSB", TaskType.SUBTASK, TaskStatus.NEW, 3);
        taskManager.saveNewSubTask(subtask2);

        EpicTask epicTask2 = new EpicTask(0, "Second epic", "Desc FE", TaskType.EPIC, TaskStatus.NEW);
        taskManager.saveNewTask(epicTask2);
        Subtask subtask3 = new Subtask(0, "Third subtask", "Desc FSB", TaskType.SUBTASK, TaskStatus.NEW, 4);
        taskManager.saveNewSubTask(subtask3);

        // Get a list with all tasks
        System.out.println("Получить список всех задач\n" + taskManager.getAllTasks());

        // Delete all tasks
        // taskManager.deleteAllTask();

        //Get all tasks by Id
        System.out.println("Получить по идентификатору\n" + taskManager.getTaskById(1));

        //5 - Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра;
        SingleTask singleTask3 = new SingleTask(1, "Single safe Task Changed", "Desc SST", TaskType.SINGLE, TaskStatus.NEW);
        taskManager.update(singleTask3);
        System.out.println("Получить по идентификатору\n" + taskManager.getTaskById(1));

        //6 - Удаление по идентификатору.");
        // taskManager.deleteById(2);

        //7 - Получение списка всех подзадач определённого эпика.");
        System.out.println("11111 - Получить список все подзадач\n" + taskManager.getAllSubtasksIdByEpicID(3));


        // how to change status
        singleTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.update(singleTask);
        System.out.println(taskManager.getTaskById(1));

    }


}
