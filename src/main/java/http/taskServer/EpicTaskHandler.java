package main.java.http.taskServer;

import main.java.description.TaskType;

import main.java.manager.TaskManager;

public class EpicTaskHandler extends TaskHandler {

    public EpicTaskHandler(TaskManager taskManager, String expectedPath, TaskType taskType) {
        super(taskManager, expectedPath, taskType);
    }
}