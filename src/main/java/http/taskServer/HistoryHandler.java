package main.java.http.taskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.java.manager.TaskManager;


public class HistoryHandler extends TaskHandler {
    private final Gson gson = new Gson();
    String expectedPath;

    public HistoryHandler(TaskManager taskManager, String expectedPath) {
        super(taskManager);
        this.expectedPath = expectedPath;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int code;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Request: " + path + " by " + method + "history" + gson.toJson(taskManager.getHistory()));
        String query = httpExchange.getRequestURI().getQuery();
        if (method.equals("GET") && query == null && path.equals(expectedPath)) {
            code = 200;
            response = gson.toJson(taskManager.getHistory());
            createResponse(httpExchange, response, code);
        } else if (method.equals("GET")){
            code = 404;
            response = "Not Found";
            createResponse(httpExchange, response, code);
        } else {
            code = 405;
            response = "Method Not Allowed";
            createResponse(httpExchange, response, code);
        }
    }
}
