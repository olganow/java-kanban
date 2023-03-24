package main.java.http;

import com.google.gson.*;
import main.java.manager.FileBackedTasksManager;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;


import java.io.File;
import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {
    private static File historyFile;
    private static final String KEY_SINGLETASKS = "singletasks";
    private static final String KEY_SUBTASKS = "subtasks";
    private static final String KEY_EPICS = "epictasks";

    private static KVTaskClient client;
    private static String serverUrl;
    private static final Gson gson =
            new GsonBuilder().create();

    public HttpTaskManager(String serverUrl) throws IOException {
        super(historyFile);
        client = new KVTaskClient(serverUrl);
    }

    @Override
    public void save() {
        client.put(KEY_SINGLETASKS, gson.toJson(singleTaskById.values()));
        System.out.println(gson.toJson(singleTaskById.values()));
        client.put(KEY_SUBTASKS, gson.toJson(subtaskById.values()));
        client.put(KEY_EPICS, gson.toJson(epicTaskById.values()));
    }

    public TaskManager loadFromHttp(String serverUrl, KVTaskClient client) throws IOException {
        TaskManager newTaskManager = new HttpTaskManager(serverUrl);
        JsonElement jsonTasks = JsonParser.parseString(client.load(KEY_SINGLETASKS));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                SingleTask task = gson.fromJson(jsonTask, SingleTask.class);
                newTaskManager.addSingleTask(task);

            }
        }

        JsonElement jsonEpics = JsonParser.parseString(client.load(KEY_EPICS));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                EpicTask task = gson.fromJson(jsonEpic, EpicTask.class);
                newTaskManager.addEpicTask(task);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(client.load(KEY_SUBTASKS));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                Subtask task = gson.fromJson(jsonSubtask, Subtask.class);
                newTaskManager.addNewSubTask(task);
            }
        }
        return newTaskManager;
    }

}
