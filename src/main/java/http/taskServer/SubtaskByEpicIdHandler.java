package main.java.http.taskServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.description.TaskType;
import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskByEpicIdHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SubtaskByEpicIdHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int code = 400;
        String response = "";
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);

        if (method.equals("GET")) {
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null) {
                try {
                    int id = Integer.parseInt(query.substring(3));
                    List<Subtask> subtasks = taskManager.getSubTasksByEpicId(id);
                    if (taskManager.validateTypeOfMapByIdContainsTaskId(id, TaskType.EPIC)) {
                        code = 200;
                        response = gson.toJson(subtasks);
                    } else {
                        code = 404;
                        response = "Not Found ";
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    code = 400;
                    response = "Bad Request, expected NumberFormat";
                } catch (NullPointerException e) {
                    code = 404;
                    response = "Null";
                } catch (ManagerSaveException e) {
                    code = 404;
                    response = "Not Found";
                }
            } else {
                code = 404;
                response = "Not Found";
            }
        }

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(code, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
