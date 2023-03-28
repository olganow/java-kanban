package main.java.http;

import com.sun.net.httpserver.HttpServer;
import main.java.description.TaskType;
import main.java.http.taskServer.*;
import main.java.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8091;
    private static final String TASKS_URL = "/tasks/";
    private static final String SINGLETASK_URL = "/tasks/task/";
    private static final String EPIC_URL = "/tasks/epic/";
    private static final String SUBTASK_URL = "/tasks/subtask/";
    private static final String SUBTASK_BY_EPIC_URL = "/tasks/subtask/epic/";
    private static final String HISTORY_URL = "/tasks/history/";
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext(TASKS_URL, new TasksPrioritizedHandler(taskManager, TASKS_URL));
        httpServer.createContext(SINGLETASK_URL, new SingleTaskHandler(taskManager, SINGLETASK_URL, TaskType.SINGLE));
        httpServer.createContext(EPIC_URL, new EpicTaskHandler(taskManager, EPIC_URL, TaskType.EPIC));
        httpServer.createContext(SUBTASK_URL, new SubtaskHandler(taskManager, SUBTASK_URL, TaskType.SUBTASK));
        httpServer.createContext(SUBTASK_BY_EPIC_URL, new SubtaskByEpicIdHandler(taskManager, SUBTASK_BY_EPIC_URL));
        httpServer.createContext(HISTORY_URL, new HistoryHandler(taskManager, HISTORY_URL));
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer  на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("На " + PORT + " порту HttpTaskServer сервер остановлен!");
    }

}
