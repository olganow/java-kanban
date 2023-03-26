package main.java.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.adapters.InstantAdapter;
import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private SingleTask singleTaskOne;
    private SingleTask singleTaskSecond;
    private EpicTask epicTaskOne;
    private EpicTask epicTaskSecond;
    private Subtask subtaskOne;
    private Subtask subtaskSecond;

    private static final String MAIN_URL = "http://localhost:8091";
    private static final String TASKS_URL = MAIN_URL + "/tasks/";
    private static final String SINGLETASK_URL = MAIN_URL + "/tasks/task/";
    private static final String EPIC_URL = MAIN_URL + "/tasks/epic/";
    private static final String SUBTASK_URL = MAIN_URL + "/tasks/subtask/";
    private static final String SUBTASK_BY_EPIC_URL = MAIN_URL + "/tasks/subtask/epic/";
    private static final String HISTORY_URL = MAIN_URL + "/tasks/history/";
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static HttpClient httpClient;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static HttpTaskManager taskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getDefaultHttpTaskManager("http://localhost:8078");
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();

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
    void stopServers() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    @DisplayName("Приоритизированные задачи - Get - 200")
    void shouldGetPrioritisedTasks() throws IOException, InterruptedException {
        URI url = URI.create(TASKS_URL);
        taskManager.addSingleTask(singleTaskOne);
        taskManager.getPrioritizedTasks();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Задача - Get - 200")
    void shouldGetSingleTask() throws IOException, InterruptedException {
        URI url = URI.create(SINGLETASK_URL);
        taskManager.addSingleTask(singleTaskOne);
        taskManager.getTaskById(singleTaskOne.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Эпик - Get - 200")
    void shouldGetEpic() throws IOException, InterruptedException {
        URI url = URI.create(EPIC_URL);
        taskManager.addEpicTask(epicTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Подзадача - Get - 200")
    void shouldGetSubtask() throws IOException, InterruptedException {
        URI url = URI.create(SUBTASK_URL);
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Задача - Get по ID - 200")
    void shouldGetSingleTaskById() throws IOException, InterruptedException {
        taskManager.addSingleTask(singleTaskOne);
        taskManager.addSingleTask(singleTaskSecond);
        int id = singleTaskSecond.getId();
        URI url = URI.create(SINGLETASK_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Эпик - Get по ID - 200")
    void shouldGetEpicById() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addEpicTask(epicTaskSecond);
        int id = epicTaskSecond.getId();
        URI url = URI.create(EPIC_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Подзадача - Get по ID - 200")
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        int id = epicTaskOne.getId();
        URI url = URI.create(SUBTASK_BY_EPIC_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Подзадача - По ID эпика  - 201")
    void shouldGetSubtaskByEpic() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addEpicTask(epicTaskSecond);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        taskManager.getSubTasksByEpicId(epicTaskOne.getId());
        int id = epicTaskOne.getId();
        URI url = URI.create(SUBTASK_BY_EPIC_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("История- Get - 404")
    void shouldNotGetHistoryByWrongUrl() throws IOException, InterruptedException {
        URI url = URI.create(HISTORY_URL + "89");
        taskManager.getHistory();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
    }

    @Test
    @DisplayName("Задача - Get - 404")
    void shouldNotGetSingleTaskByWrongUrl() throws IOException, InterruptedException {
        URI url = URI.create(SINGLETASK_URL + "/879a");
        taskManager.addSingleTask(singleTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
    }

    @Test
    @DisplayName("Эпик - Get - 404")
    void shouldNotGetEpicByWrongUrl() throws IOException, InterruptedException {
        URI url = URI.create(EPIC_URL + "1");
        taskManager.addEpicTask(epicTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
    }

    @Test
    @DisplayName("Подзадача - Get - 404")
    void shouldNotGetSubtaskByWrongUrl() throws IOException, InterruptedException {
        URI url = URI.create(SUBTASK_URL + "000");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
    }

    @Test
    @DisplayName("Задача - Get по некорректному цифровому ID - 404")
    void shouldNotGetSingleTaskByIdByWrongId() throws IOException, InterruptedException {
        taskManager.addSingleTask(singleTaskOne);
        taskManager.addSingleTask(singleTaskSecond);
        int id = singleTaskSecond.getId() + 999;
        URI url = URI.create(SINGLETASK_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
    }

    @Test
    @DisplayName("Эпик - Get по некорректному ID с буквами - 400")
    void shouldNotGetEpicByIdByWrongId() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addEpicTask(epicTaskSecond);
        int id = epicTaskSecond.getId();
        URI url = URI.create(EPIC_URL + "?id=" + id + "abc");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Bad Request, expected NumberFormat", response.body());
    }

    @Test
    @DisplayName("Подзадача - Get по некорректному пути без ID - 400")
    void shouldNotGetSubtaskByIdByWrongId() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        URI url = URI.create(SUBTASK_BY_EPIC_URL + "?id=");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Bad Request, expected NumberFormat", response.body());
    }

    @Test
    @DisplayName("Подзадача - По некорректному ID эпика  - 200")
    void shouldNotGetSubtaskByEpicByWrongId() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addEpicTask(epicTaskSecond);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        taskManager.getSubTasksByEpicId(epicTaskOne.getId());
        int id = epicTaskOne.getId();
        URI url = URI.create(SUBTASK_BY_EPIC_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Задача - POST - 201")
    void shouldUpdateSingleTask() throws IOException, InterruptedException {
        URI url = URI.create(SINGLETASK_URL);
        taskManager.addSingleTask(singleTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(singleTaskOne)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Эпик - POST - 201")
    void shouldUpdateEpic() throws IOException, InterruptedException {
        URI url = URI.create(EPIC_URL);
        taskManager.addEpicTask(epicTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTaskOne)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Подзадача - POST - 201")
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        URI url = URI.create(SUBTASK_URL);
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtaskOne)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Задача - POST - 201")
    void shouldNotUpdateSingleTaskWithNull() throws IOException, InterruptedException {
        URI url = URI.create(SINGLETASK_URL);
        taskManager.addSingleTask(singleTaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(singleTaskOne)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Эпик - POST - 406")
    void shouldNotUpdateEpicWithBadJson() throws IOException, InterruptedException {
        URI url = URI.create(EPIC_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(999)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    @DisplayName("Подзадача - POST - 201")
    void shouldNotUpdateSubtaskWithBadJson() throws IOException, InterruptedException {
        URI url = URI.create(SUBTASK_URL);
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(999)))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    @DisplayName("Задача - Delete - 200")
    void shouldDeleteSingleTaskById() throws IOException, InterruptedException {
        taskManager.addSingleTask(singleTaskOne);
        int id = singleTaskSecond.getId();
        URI url = URI.create(SINGLETASK_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Task with id=0 has been deleted", response.body());
        assertTrue(taskManager.getAllSingleTasks().isEmpty());

    }

    @Test
    @DisplayName("Эпик - Delete - 200")
    void shouldDeleteEpic() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        int id = epicTaskOne.getId();
        URI url = URI.create(EPIC_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Task with id=0 has been deleted", response.body());
        assertTrue(taskManager.getAllEpicTasks().isEmpty());
    }

    @Test
    @DisplayName("Подзадача - Delete - 200")
    void shouldDeleteSubtask() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        int id = epicTaskOne.getId();
        URI url = URI.create(SUBTASK_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Task with id=0 has been deleted", response.body());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    @DisplayName("Задача - Delete c невалидным Id - 400")
    void shouldDeleteSingleTaskWithInvalidId() throws IOException, InterruptedException {
        taskManager.addSingleTask(singleTaskOne);
        URI url = URI.create(SINGLETASK_URL + "?id=");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Bad Request, expected NumberFormat", response.body());
        assertFalse(taskManager.getAllSingleTasks().isEmpty());

    }

    @Test
    @DisplayName("Эпик - Delete - 400")
    void shouldDeleteEpicWithInvalidId() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        URI url = URI.create(EPIC_URL + "?id=" + null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Bad Request, expected NumberFormat", response.body());
        assertFalse(taskManager.getAllEpicTasks().isEmpty());
    }

    @Test
    @DisplayName("Подзадача - Delete с некорректным - 404")
    void shouldDeleteSubtaskWithInvalidId() throws IOException, InterruptedException {
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addNewSubTask(subtaskOne);
        int id = epicTaskOne.getId() + 56;
        URI url = URI.create(SUBTASK_URL + "?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Not Found", response.body());
        assertFalse(taskManager.getAllSubtasks().isEmpty());
    }
}