package main.java.http;

import com.google.gson.*;
import main.java.manager.FileBackedTasksManager;
import main.java.manager.InMemoryTaskManager;

import main.java.task.SingleTask;

import java.io.File;
import java.io.IOException;


public class HttpTaskManager extends FileBackedTasksManager {

    private static File historyFile;
    public KVTaskClient client;
    Gson gson = new Gson();

    public HttpTaskManager(String serverURL) throws IOException, InterruptedException {
        super(historyFile);
        client = new KVTaskClient(serverURL);
    }

    @Override
    public void addSingleTask(SingleTask singleTask) {
        int taskId = singleTask.getId();
        for (int id : singleTaskById.keySet()) {
            client.put(String.valueOf(id), singleTask.getClass().getSimpleName() + "//" + gson.toJson(singleTask));

            if (getNextFreedI() < taskId) {
                setNextFreedId(taskId);
            }
        }
        client.put("maxID", String.valueOf(taskId));
        client.put("history", historyManager.getHistory().toString());
    }

    public KVTaskClient getClient() {
        return client;
    }

    private static int getNextFreedI() {
        InMemoryTaskManager.TaskIdGenerator generatorId = new TaskIdGenerator();
        return generatorId.getNextFreedI();
    }

    private static void setNextFreedId(int id) {
        InMemoryTaskManager.TaskIdGenerator generatorId = new TaskIdGenerator();
        generatorId.setNextFreedId(id);

    }
}
