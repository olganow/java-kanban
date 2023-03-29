package main.java.http.taskServer;

import com.sun.net.httpserver.HttpExchange;

import main.java.description.TaskType;
import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.Subtask;

import java.util.List;

public class SubtaskByEpicIdHandler extends TaskHandler {

    public SubtaskByEpicIdHandler(TaskManager taskManager, String expectedPath) {
        super(taskManager);
        this.expectedPath = expectedPath;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int code;
        String response;
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
                } catch (ManagerSaveException e) {
                    code = 404;
                    response = "Not Found";
                    createResponse(httpExchange, response, code);
                }
            } else {
                code = 404;
                response = "Not Found";
                createResponse(httpExchange, response, code);
            }
        } else {
            code = 405;
            response = "Method Not Allowed";
            createResponse(httpExchange, response, code);
        }


    }
}
