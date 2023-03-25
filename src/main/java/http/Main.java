package main.java.http;

import com.google.gson.Gson;
import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;

import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Это программа Трекер задач Франкенштейн. Версия1");

        Gson gson = new Gson();
        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();


        HttpTaskManager taskManager = (HttpTaskManager) Managers.getDefaultHttpTaskManager("http://localhost:8078");
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");

        SingleTask singleTask1 = new SingleTask("Single safe Task", "Desc SST",
                TaskStatus.NEW, Instant.ofEpochMilli(1704056400000L), 8400L);
        taskManager.addSingleTask(singleTask1);

        SingleTask singleTask2 = new SingleTask("Single 2", "Desc SST",
                TaskStatus.NEW, Instant.ofEpochMilli(153000L), 8400L);
        taskManager.addSingleTask(singleTask2);


        EpicTask epicTask1 = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask1);
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB",
                TaskStatus.NEW, null, 500, epicTask1.getId());
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB",
                TaskStatus.NEW, null, 500, epicTask1.getId());
        taskManager.addNewSubTask(subtask2);
        System.out.println("Получить список всех эпиков\n" + taskManager.getAllEpicTasks());

        EpicTask epicTask2 = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask2);

        System.out.println("Получить список всех эпиков\n" + taskManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + taskManager.getAllSubtasks());

        System.out.println("Получить по ID\n" + taskManager.getTaskById(0));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(1));
        System.out.println("Получить по ID\n" + taskManager.getTaskById(2));

        HttpTaskServer taskServer = Managers.getDefaultHttpTaskServer(taskManager);
        taskServer.start();

        System.out.println("Для завершения нажмите 0");
        if (new Scanner(System.in).nextInt() == 0) {
            taskServer.stop();
            kvServer.stop();
        }

    }
}