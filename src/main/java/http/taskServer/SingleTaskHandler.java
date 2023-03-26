package main.java.http.taskServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.description.TaskType;
import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.SingleTask;
import main.java.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SingleTaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SingleTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int code;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);

        switch (method) {

            case "GET":
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null && path.equals("/tasks/task/")) {
                    code = 200;
                    String jsonString = gson.toJson(taskManager.getAllSingleTasks());
                    response = gson.toJson(jsonString);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(3));
                        Task task = taskManager.getTaskById(id);
                        boolean isQueryStartCorrect = query.substring(0, 3).equals("id=");
                        if (taskManager.validateTypeOfMapByIdContainsTaskId(id, TaskType.SINGLE) && isQueryStartCorrect) {
                            code = 200;
                            response = gson.toJson(task);
                        } else {
                            code = 404;
                            response = "Not Found ";
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        code = 400;
                        response = "Bad Request, expected NumberFormat";
                    } catch (ManagerSaveException | NullPointerException e) {
                        code = 404;
                        response = "Not Found";
                    }
                }
                break;

            case "POST":
                String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SingleTask task = gson.fromJson(bodyRequest, SingleTask.class);
                    int id = task.getId();
                    if (taskManager.validateTypeOfMapByIdContainsTaskId(id, TaskType.SINGLE)) {
                        taskManager.updateSingleTask(task);
                        code = 201;
                        response = "Task with id=" + id + " has been updated";
                    } else {
                        taskManager.addSingleTask(task);
                        int newId = task.getId();
                        code = 201;
                        response = "Task with id=" + newId + " has been created";
                    }
                } catch (JsonSyntaxException e) {
                    code = 406;
                    response = "Not Acceptable Json Syntax in the request";
                } catch (NullPointerException e) {
                    code = 404;
                    response = "Null";
                }
                break;

            case "DELETE":
                query = httpExchange.getRequestURI().getQuery();
                try {
                    if (query == null && path.equals("/tasks/task/")) {
                        taskManager.deleteAllSingleTask();
                        code = 200;
                        response = "All single tasks have been deleted";
                    } else {
                        int id = Integer.parseInt(query.substring(3));
                        taskManager.deleteById(id);
                        code = 200;
                        response = "Task with id=" + id + " has been deleted";
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    code = 400;
                    response = "Bad Request, expected NumberFormat";
                } catch (ManagerSaveException | NullPointerException e) {
                    code = 404;
                    response = "Not Found";
                }

                break;

            default:
                code = 404;
                response = "Not Found";
        }

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(code, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }


    }
}
