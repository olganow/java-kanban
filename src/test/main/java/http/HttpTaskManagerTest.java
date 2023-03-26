package main.java.http;

import com.google.gson.Gson;
import main.java.description.TaskStatus;
import main.java.manager.*;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    private static KVServer kvServer;
    private static HttpTaskManager taskManager;
    private SingleTask singleTaskOne;
    private SingleTask singleTaskSecond;
    private EpicTask epicTaskOne;
    private EpicTask epicTaskSecond;
    private Subtask subtaskOne;
    private Subtask subtaskSecond;
    private KVTaskClient kvTaskClient;
    private Gson gson;

    @BeforeEach
    void beforeEach() throws IOException {
        gson = new Gson();
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient("http://localhost:8078");

        taskManager = Managers.getDefaultHttpTaskManager("http://localhost:8078");

        singleTaskOne = new SingleTask("First single task for testing", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(400000L), 8400L);
        singleTaskSecond = new SingleTask("First single task for testing", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(1099400000L), 10L);

        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        epicTaskSecond = new EpicTask("First epic for testing", "Desc FE for testing ");

        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 707568400L, epicTaskSecond.getId());
        subtaskSecond = new Subtask("Second subtask for testing", "Desc SSB", TaskStatus.NEW,
                Instant.ofEpochMilli(99173857900000L), 707568400L, epicTaskSecond.getId());
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    @DisplayName("Сохранение задачи")
    void shouldSaveSingleTask() {
        assert taskManager != null;
        taskManager.addSingleTask(singleTaskOne);
        taskManager.addSingleTask(singleTaskSecond);
        int id = singleTaskSecond.getId();
        Task ExpecterResultTask = taskManager.getTaskById(id);
        assertEquals(ExpecterResultTask, singleTaskSecond, "Task was not saved");
    }

    @Test
    @DisplayName("Сохранение эпика")
    void shouldSaveEpicTask() {
        assert taskManager != null;
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addEpicTask(epicTaskSecond);
        int id = epicTaskSecond.getId();
        Task ExpecterResultTask = taskManager.getTaskById(id);
        assertEquals(ExpecterResultTask, epicTaskSecond, "Task was not saved");
    }

    @Test
    @DisplayName("Сохранение подзадачи")
    void shouldSaveSubtask() {
        assert taskManager != null;
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        int id = subtaskSecond.getId();
        Task ExpecterResultTask = taskManager.getTaskById(id);
        assertEquals(ExpecterResultTask, subtaskSecond, "Task was not saved");
    }

    @Test
    @DisplayName("Сохранение и выгрузка задачи")
    void shouldLoadSingleTask() {
        kvTaskClient.put("1", gson.toJson(singleTaskOne));
        String expectedResult = "SingleTask{id=0, name='First single task for testing', description='Desc AST', type='SINGLE', status='NEW', start time ='1970-01-01T00:06:40Z', duration ='8400'}\n";
        String actualResult = (gson.fromJson(kvTaskClient.load("1"), SingleTask.class).toString());
        assertEquals(expectedResult, actualResult, "Task was not loaded");
    }

    @Test
    @DisplayName("Сохранение и выгрузка эпика")
    void shouldLoadEpicTask() {
        kvTaskClient.put("1", gson.toJson(epicTaskOne));
        String expectedResult = "EpicTask{id=0, name='First epic for testing', description='Desc FE for testing ', type='EPIC', status=NEW, start time ='null', duration ='0', subtaskList=[]}\n";
        String actualResult = (gson.fromJson(kvTaskClient.load("1"), EpicTask.class).toString());
        assertEquals(expectedResult, actualResult, "Task was not loaded");
    }

    @Test
    @DisplayName("Сохранение и выгрузка подзадачи")
    void shouldLoadSubtask() {
        kvTaskClient.put("1", gson.toJson(subtaskOne));
        String expectedResult = "SubTask{id=0, name='First subtask for testing', description='Desc FSB', type='SUBTASK', status=NEW, start time ='1975-03-12T12:05:00Z', duration ='707568400', epic id='0'}\n";
        String actualResult = (gson.fromJson(kvTaskClient.load("1"), Subtask.class).toString());
        System.out.println(gson.fromJson(kvTaskClient.load("1"), Subtask.class));
        assertEquals(expectedResult, actualResult, "Task was not loaded");
    }
}