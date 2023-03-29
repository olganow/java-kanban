package main.java.manager;

import main.java.description.TaskStatus;

import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import org.junit.jupiter.api.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final String path = "src/test/resources/history_data_test.csv";

    private File file = new File(path);
    private SingleTask singleTask;
    private EpicTask epicTask;
    private Subtask subtask;

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(file);


        singleTask = new SingleTask("Another safe Task", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(1704056400000L), 707568400L);
        singleTask.setId(0);
        epicTask = new EpicTask("First epic", "Desc FE");
        epicTask.setId(1);
        subtask = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 707568400L, epicTask.getId());
        subtask.setId(2);
    }

    @AfterEach
    public void afterEach() {
        taskManager.deleteAllTask();
    }

    @Test
    @DisplayName("Сохранение в файл")
    public void shouldSaveToFile() throws IOException {
        String path = "src/test/resources/history_for_data_test.csv";
        File file = new File(path);
        taskManager = new FileBackedTasksManager(file);
        taskManager.addSingleTask(singleTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);

        String actualResult = Files.readString(Paths.get(path));
        String expectedResult = "id,type,name,status,description,startTime,duration,endTime,epic\n" +
                "0,SINGLE,Another safe Task,NEW,Desc AST,2023-12-31T21:00:00Z,707568400,2046-06-03T07:46:40Z\n" +
                "1,EPIC,First epic,NEW,Desc FE,1975-03-12T12:05:00Z,707568400,1997-08-12T22:51:40Z\n" +
                "2,SUBTASK,First subtask for testing,NEW,Desc FSB,1975-03-12T12:05:00Z,707568400,1997-08-12T22:51:40Z,1\n" +
                "\n";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Пустой список задач")
    public void shouldSaveEmptyTaskListToFile() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            String path = "src/test/resources/history_for_data_test.csv";
            File file = new File(path);
            taskManager = new FileBackedTasksManager(file);
            singleTask = null;
            taskManager.addSingleTask(singleTask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }


    @Test
    @DisplayName("Эпик без подзадач")
    public void shouldSaveEpicWithoutSubtaskToFile() throws IOException {
        String path = "src/test/resources/history_for_data_test.csv";
        File file = new File(path);
        taskManager = new FileBackedTasksManager(file);
        taskManager.addEpicTask(epicTask);

        String actualResult = Files.readString(Paths.get(path));
        String expectedResult = "id,type,name,status,description,startTime,duration,endTime,epic\n" +
                "0,EPIC,First epic,NEW,Desc FE,null,0,null\n" + "\n";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Пустой список истории")
    public void shouldSaveEpicEmptyHistory() {
        String path = "src/test/resources/history_empty_test.csv";
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertTrue(fileManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Список задач после выгрузки совпадает")
    public void shouldSaveEqualSingleTaskList() {
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertEquals(taskManager.getAllSingleTasks(), fileManager.getAllSingleTasks());
    }

    @Test
    @DisplayName("Список эпиков после выгрузки совпадает")
    public void shouldSaveEqualEpicList() {
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertEquals(taskManager.getAllSingleTasks(), fileManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Список сабтасок после выгрузки совпадает")
    public void shouldSaveEqualSubtaskList() {
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertEquals(taskManager.getAllSingleTasks(), fileManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Список историй после выгрузки совпадает")
    public void shouldSaveEqualList() {
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertEquals(taskManager.getHistory(), fileManager.getHistory());
    }

    @Test
    @DisplayName("Список отсортированных задач после выгрузки совпадает")
    public void shouldSaveEqualSortedList() {
        File file = new File(path);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        assertEquals(taskManager.getPrioritizedTasks(), fileManager.getPrioritizedTasks());
    }
}
