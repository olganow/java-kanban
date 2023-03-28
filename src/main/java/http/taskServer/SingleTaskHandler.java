package main.java.http.taskServer;


import main.java.description.TaskType;
import main.java.manager.TaskManager;


public class SingleTaskHandler extends TaskHandler {

    public SingleTaskHandler(TaskManager taskManager, String expectedPath, TaskType taskType) {
        super(taskManager, expectedPath, taskType);
    }
}