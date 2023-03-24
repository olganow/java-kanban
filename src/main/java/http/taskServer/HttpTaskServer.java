package main.java.http.taskServer;

import com.sun.net.httpserver.HttpServer;
import main.java.manager.HistoryManager;
import main.java.manager.Managers;
import main.java.manager.TaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8091;
    private final TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);


        httpServer.createContext("/tasks/", new TasksHandler(taskManager));
        httpServer.createContext("/tasks/task/", new SingleTaskHandler(taskManager));

    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer  на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks/");
        //   System.out.println("API_TOKEN: " + apiToken);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("На " + PORT + " порту HttpTaskServer сервер остановлен!");
    }

}
