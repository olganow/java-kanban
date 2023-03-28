package main.java.http.taskServer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.description.TaskType;
import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class TaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;
    private final Gson gson = new Gson();
    private String expectedPath;
    private TaskType taskType;
    private String response;

    private int code;

    public TaskHandler(TaskManager taskManager, String expectedPath, TaskType taskType) {
        this.taskManager = taskManager;
        this.expectedPath = expectedPath;
        this.taskType = taskType;
    }

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
                createResponse(httpExchange, "Not found", 404);
        }

    }

    private void getTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
        if (query == null && path.equals(expectedPath)) {
            String jsonString = "";
            switch (taskType) {
                case SINGLE:
                    jsonString = gson.toJson(taskManager.getAllSingleTasks());
                    break;
                case SUBTASK:
                    jsonString = gson.toJson(taskManager.getAllSubtasks());
                    break;
                case EPIC:
                    jsonString = gson.toJson(taskManager.getAllEpicTasks());
                    break;
            }

            code = 200;
            response = gson.toJson(jsonString);
            createResponse(httpExchange, response, code);
        } else {
            try {
                int id = Integer.parseInt(query.substring(3));
                Task task = taskManager.getTaskById(id);
                boolean isQueryStartCorrect = query.substring(0, 3).equals("id=");
                if (taskManager.validateTypeOfMapByIdContainsTaskId(id, taskType) && isQueryStartCorrect) {
                    code = 200;
                    response = gson.toJson(task);
                    createResponse(httpExchange, response, code);
                } else {
                    code = 404;
                    response = "Not Found ";
                    createResponse(httpExchange, response, code);
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                code = 400;
                response = "Bad Request, expected NumberFormat";
                createResponse(httpExchange, response, code);
            } catch (ManagerSaveException | NullPointerException e) {
                code = 404;
                response = "Not Found";
                createResponse(httpExchange, response, code);
            }
        }
    }

    private void postTask(HttpExchange httpExchange, TaskType taskType) {
        String bodyRequest;
        Task task = null;
        try {
            bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (bodyRequest == null) {
                code = 404;
                response = "Not Found";
                createResponse(httpExchange, response, code);
                return;
            }
            switch (taskType) {
                case SINGLE:
                    task = gson.fromJson(bodyRequest, SingleTask.class);
                    break;
                case SUBTASK:
                    task = gson.fromJson(bodyRequest, Subtask.class);
                    break;
                case EPIC:
                    task = gson.fromJson(bodyRequest, EpicTask.class);
                    break;
            }

            int id = task.getId();
            if (taskManager.validateTypeOfMapByIdContainsTaskId(id, taskType)) {
                switch (taskType) {
                    case SINGLE:
                        taskManager.updateSingleTask((SingleTask) task);
                        break;
                    case SUBTASK:
                        taskManager.updateSubtask((Subtask) task);
                        break;
                    case EPIC:
                        taskManager.updateEpicTask((EpicTask) task);
                        break;
                }
                code = 201;
                response = "Task with id=" + id + " has been updated";
                createResponse(httpExchange, response, code);
            } else {
                switch (taskType) {
                    case SINGLE:
                        taskManager.addSingleTask((SingleTask) task);
                        break;
                    case SUBTASK:
                        taskManager.addNewSubTask((Subtask) task);
                        break;
                    case EPIC:
                        taskManager.addEpicTask((EpicTask) task);
                        break;
                }
                int newId = task.getId();
                code = 201;
                response = "Task with id=" + newId + " has been created";
                createResponse(httpExchange, response, code);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            code = 406;
            response = "Not Acceptable Json Syntax in the request";
            createResponse(httpExchange, response, code);
        } catch (NullPointerException e) {
            code = 404;
            response = "Null";
            createResponse(httpExchange, response, code);
        }
    }

    private void deleteTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
        try {
            if (query == null && path.equals(expectedPath)) {
                switch (taskType) {
                    case SINGLE:
                        taskManager.deleteAllSingleTask();
                        break;
                    case SUBTASK:
                        taskManager.deleteAllSubtask();
                        break;
                    case EPIC:
                        taskManager.deleteAllEpicTask();
                        break;
                }
                code = 200;
                response = "All tasks have been deleted";
                createResponse(httpExchange, response, code);
            } else {
                int id = Integer.parseInt(query.substring(3));
                taskManager.deleteById(id);
                code = 200;
                response = "Task with id=" + id + " has been deleted";
                createResponse(httpExchange, response, code);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            code = 400;
            response = "Bad Request, expected NumberFormat";
            createResponse(httpExchange, response, code);
        } catch (ManagerSaveException | NullPointerException e) {
            code = 404;
            response = "Not Found";
            createResponse(httpExchange, response, code);
        }
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
