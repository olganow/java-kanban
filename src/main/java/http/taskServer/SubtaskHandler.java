package main.java.http.taskServer;

import main.java.description.TaskType;

import main.java.manager.TaskManager;

public class SubtaskHandler extends TaskHandler {

    public SubtaskHandler(TaskManager taskManager, String expectedPath, TaskType taskType) {
        super(taskManager, expectedPath, taskType);
    }
}