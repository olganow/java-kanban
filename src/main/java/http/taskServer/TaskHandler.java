package main.java.http.taskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.description.TaskType;
import main.java.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public abstract class TaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;
    protected final Gson gson = new Gson();
    protected String expectedPath;
    protected TaskType taskType;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);
        String query = httpExchange.getRequestURI().getQuery();
        switch (method) {

            case "GET":
                getTask(httpExchange, path, taskType, query);
                break;

            case "POST":
                postTask(httpExchange, taskType);
                break;

            case "DELETE":
                deleteTask(httpExchange, path, taskType, query);
                break;

            default:
                int code = 405;
                String response = "Method Not Allowed";
                createResponse(httpExchange, response, code);
        }
    }

    protected void getTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
    }

    protected void postTask(HttpExchange httpExchange, TaskType taskType) {
    }

    protected void deleteTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
    }

    public String getBodyRequest(HttpExchange httpExchange) {
        String bodyRequest;
        try {
            bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bodyRequest;
    }

    public static void createResponse(HttpExchange httpExchange, String response, int code) {
        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        try {
            httpExchange.sendResponseHeaders(code, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
