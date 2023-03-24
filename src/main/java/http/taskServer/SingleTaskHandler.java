package main.java.http.taskServer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.manager.TaskManager;
import main.java.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SingleTaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final  Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public SingleTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int code = 404;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);

        switch (method) {

            case "GET":
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    code = 200;
                    String jsonString = gson.toJson(taskManager.getAllSingleTasks());
                    response = gson.toJson(jsonString);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Task task = taskManager.getTaskById(id);
                        if (task != null || taskManager.getAllSubtasks().contains(id)) {
                            code = 200;
                            response = gson.toJson(task);
                        } else  {
                            code = 404;
                            response = "Not Found";
                        }
                    }catch (NumberFormatException | NullPointerException e) {
                        code = 400;
                        response = "Bad Request";
                    }
                }

                break;
            case "POST":
                response = "Bad Request";

                break;
            case "DELETE":
                response = "Bad Request";

                break;

            default:
                response = "Not Found";
        }

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(code, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
