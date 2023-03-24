package main.java.http;

import com.google.gson.Gson;
import main.java.description.TaskStatus;
import main.java.http.taskServer.HttpTaskServer;
import main.java.manager.Managers;
import main.java.task.SingleTask;

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

            HttpTaskServer taskServer = Managers.getDefaultHttpTaskServer(taskManager);
            taskServer.start();

            System.out.println("Для завершения нажмите 0");
            if (new Scanner(System.in).nextInt() == 0) {
                taskServer.stop();
                kvServer.stop();
            }

    }
}