package main.java.http;

import com.google.gson.*;
import main.java.manager.FileBackedTasksManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;


import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String KEY_SINGLETASKS = "singletasks";
    private static final String KEY_SUBTASKS = "subtasks";
    private static final String KEY_EPICS = "epictasks";
    private static final String KEY_HISTORY = "history";
    private final KVTaskClient client;
    private static final Gson gson = new GsonBuilder().create();

    public HttpTaskManager(String serverUrl) {
        this(serverUrl, false);
    }

    public HttpTaskManager(String serverUrl, boolean load) {
        super(null);
        client = new KVTaskClient(serverUrl);
        if (load) {
            loadFromHttp();
        }
    }

    @Override
    protected void save() {
        client.put(KEY_SINGLETASKS, gson.toJson(singleTaskById.values()));
        client.put(KEY_SUBTASKS, gson.toJson(subtaskById.values().stream().collect(Collectors.toList())));
        client.put(KEY_EPICS, gson.toJson(epicTaskById.values().stream().collect(Collectors.toList())));
        client.put(KEY_HISTORY, gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList())));

        System.out.println("HttpTaskManager: задачи сохранены на KVTaskClient");
    }

    private void loadFromHttp() {
        JsonElement jsonTasks = JsonParser.parseString(client.load(KEY_SINGLETASKS));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                SingleTask task = gson.fromJson(jsonTask, SingleTask.class);
                int taskID = getTaskId(task);
                if (taskIdGenerator.getNextFreedI() < taskID) {
                    taskIdGenerator.setNextFreedId(taskID);
                }
                singleTaskById.put(taskID, task);
                sortedTasks.add(task);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(client.load(KEY_EPICS));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                EpicTask task = gson.fromJson(jsonEpic, EpicTask.class);
                int taskID = getTaskId(task);
                if (taskIdGenerator.getNextFreedI() < taskID) {
                    taskIdGenerator.setNextFreedId(taskID);
                }
                epicTaskById.put(taskID, task);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(client.load(KEY_SUBTASKS));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                Subtask task = gson.fromJson(jsonSubtask, Subtask.class);
                int taskID = getTaskId(task);
                if (taskIdGenerator.getNextFreedI() < taskID) {
                    taskIdGenerator.setNextFreedId(taskID);
                }
                subtaskById.put(taskID, task);
                sortedTasks.add(task);
            }
        }

        JsonElement jsonHistoryList = JsonParser.parseString(client.load(KEY_HISTORY));
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonTaskId : jsonHistoryArray) {
                int taskId = jsonTaskId.getAsInt();
                if (this.subtaskById.containsKey(taskId)) {
                    this.historyManager.add(subtaskById.get(taskId));
                } else if (this.epicTaskById.containsKey(taskId)) {
                    this.historyManager.add(epicTaskById.get(taskId));
                } else if (this.singleTaskById.containsKey(taskId)) {
                    this.historyManager.add(singleTaskById.get(taskId));
                }
            }
        }
    }

    private int getTaskId(Task task) {
        return task.getId();
    }
}
