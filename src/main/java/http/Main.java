package main.java.http;

import com.google.gson.Gson;
import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.task.SingleTask;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            KVServer kvServer = Managers.getDefaultKVServer();
            kvServer.start();
            HttpTaskManager taskManager =  Managers.getDefaultHttpTaskManager("http://localhost:8078");
            KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");

            SingleTask singleTask1 = new SingleTask("Single safe Task", "Desc SST",
                    TaskStatus.NEW, Instant.ofEpochMilli(1704056400000L), 8400L);
            taskManager.addSingleTask(singleTask1);
            SingleTask singleTask2 = new SingleTask("Single 2", "Desc SST",
                    TaskStatus.NEW, Instant.ofEpochMilli(153000L), 8400L);
            taskManager.addSingleTask(singleTask2);

            System.out.println("Печать всех задач");

            System.out.println(gson.toJson(taskManager.getAllSingleTasks()));
            System.out.println("load");
            //String serverUrl, KVTaskClient client

            taskManager.loadFromHttp("http://localhost:8078", kvTaskClient);


            kvServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

