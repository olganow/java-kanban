package main.java.http.taskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;

public class TasksPrioritizedHandler extends TaskHandler {
    // private TaskManager taskManager;
    private final Gson gson = new Gson();
    String expectedPath;

    public TasksPrioritizedHandler(TaskManager taskManager, String expectedPath) {
        super(taskManager);
        this.expectedPath = expectedPath;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int code = 404;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method);
        String query = httpExchange.getRequestURI().getQuery();
        if (method.equals("GET") && query == null && path.equals(expectedPath)) {
            code = 200;
            response = gson.toJson(taskManager.getPrioritizedTasks());
            createResponse(httpExchange, response, code);
        } else {
            response = "Not Found";
            createResponse(httpExchange, response, code);
        }


    }


}
