package main.java.http.taskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TasksPrioritizedHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public TasksPrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int code = 404;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);

        if (method.equals("GET")) {
            code = 200;
            response = gson.toJson(taskManager.getPrioritizedTasks());
        } else {
            response = "Not Found";
        }

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(code, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
