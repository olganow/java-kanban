package main.java.http;

import com.sun.net.httpserver.HttpServer;
import main.java.http.taskServer.*;
import main.java.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8091;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/", new TasksPrioritizedHandler(taskManager));
        httpServer.createContext("/tasks/task/", new SingleTaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicTaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/epic/", new SubtaskByEpicIdHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));
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
