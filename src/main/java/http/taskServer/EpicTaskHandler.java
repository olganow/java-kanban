package main.java.http.taskServer;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import main.java.description.TaskType;

import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.Task;

public class EpicTaskHandler extends TaskHandler {

    public EpicTaskHandler(TaskManager taskManager, String expectedPath, TaskType taskType) {
        super(taskManager);
        this.expectedPath = expectedPath;
        this.taskType = taskType;
    }

    protected void getTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
        int code;
        String response;
        try {
            if (query == null && path.equals(expectedPath)) {
                String jsonString = gson.toJson(taskManager.getAllEpicTasks());
                code = 200;
                response = gson.toJson(jsonString);
                createResponse(httpExchange, response, code);
            } else if (query != null) {
                int id = Integer.parseInt(query.substring(3));
                Task task = taskManager.getTaskById(id);
                boolean isQueryStartCorrect = query.substring(0, 3).equals("id=");
                if (taskManager.validateTypeOfMapByIdContainsTaskId(id, taskType) && isQueryStartCorrect) {
                    code = 200;
                    response = gson.toJson(task);
                    createResponse(httpExchange, response, code);
                } else {
                    code = 404;
                    response = "Not Found";
                    createResponse(httpExchange, response, code);
                }
            } else {
                code = 404;
                response = "Not Found";
                createResponse(httpExchange, response, code);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            code = 400;
            response = "Bad Request, expected NumberFormat";
            createResponse(httpExchange, response, code);
        } catch (ManagerSaveException e) {
            code = 404;
            response = "Not Found";
            createResponse(httpExchange, response, code);
        }
    }

    protected void postTask(HttpExchange httpExchange, TaskType taskType) {
        int code;
        String response;
        String bodyRequest;
        Task task;
        try {
            bodyRequest = getBodyRequest(httpExchange);
            if (bodyRequest.isEmpty()) {
                code = 400;
                response = "BAD_REQUEST";
                createResponse(httpExchange, response, code);
                return;
            }
            task = gson.fromJson(bodyRequest, EpicTask.class);
            int id = task.getId();
            if (taskManager.validateTypeOfMapByIdContainsTaskId(id, taskType)) {
                taskManager.updateEpicTask((EpicTask) task);
                code = 201;
                response = "Task with id=" + id + " has been updated";
                createResponse(httpExchange, response, code);
            } else {
                taskManager.addEpicTask((EpicTask) task);
                int newId = task.getId();
                code = 201;
                response = "Task with id=" + newId + " has been created";
                createResponse(httpExchange, response, code);
            }
        } catch (JsonSyntaxException e) {
            code = 406;
            response = "Not Acceptable Json Syntax in the request";
            createResponse(httpExchange, response, code);
        }
    }

    @Override
    protected void deleteTask(HttpExchange httpExchange, String path, TaskType taskType, String query) {
        int code;
        String response;
        try {
            if (query == null && path.equals(expectedPath)) {
                taskManager.deleteAllEpicTask();
                code = 200;
                response = "All tasks have been deleted";
                createResponse(httpExchange, response, code);
            } else if (query != null) {
                int id = Integer.parseInt(query.substring(3));
                taskManager.deleteById(id);
                code = 200;
                response = "Task with id=" + id + " has been deleted";
                createResponse(httpExchange, response, code);
            } else {
                code = 404;
                response = "Not Found";
                createResponse(httpExchange, response, code);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            code = 400;
            response = "BAD_REQUEST";
            createResponse(httpExchange, response, code);
        } catch (ManagerSaveException e) {
            code = 404;
            response = "Not Found";
            createResponse(httpExchange, response, code);
        }
    }
}