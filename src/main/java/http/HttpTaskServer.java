package main.java.http;

import com.sun.net.httpserver.HttpServer;
import main.java.description.TaskType;
import main.java.http.taskServer.*;
import main.java.manager.Managers;
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

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultHttpTaskManager("http://localhost:8078/"));
    }

    public HttpTaskServer(TaskManager httpTaskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        this.httpServer.createContext(TASKS_URL, new TasksPrioritizedHandler(httpTaskManager, TASKS_URL));
        this.httpServer.createContext(SINGLETASK_URL, new SingleTaskHandler(httpTaskManager, SINGLETASK_URL, TaskType.SINGLE));
        this.httpServer.createContext(EPIC_URL, new EpicTaskHandler(httpTaskManager, EPIC_URL, TaskType.EPIC));
        this.httpServer.createContext(SUBTASK_URL, new SubtaskHandler(httpTaskManager, SUBTASK_URL, TaskType.SUBTASK));
        this.httpServer.createContext(SUBTASK_BY_EPIC_URL, new SubtaskByEpicIdHandler(httpTaskManager, SUBTASK_BY_EPIC_URL));
        this.httpServer.createContext(HISTORY_URL, new HistoryHandler(httpTaskManager, HISTORY_URL));
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
