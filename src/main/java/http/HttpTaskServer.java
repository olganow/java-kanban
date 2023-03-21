package main.java.http;

import com.sun.net.httpserver.HttpServer;
import main.java.manager.HistoryManager;
import main.java.manager.Managers;
import main.java.manager.TaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }


}
