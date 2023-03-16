import main.java.description.TaskStatus;
import main.java.manager.FileBackedTasksManager;

import main.java.manager.ManagerSaveException;
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
    public final String path = "src/test/resources/history_data_test.csv";

    File file = new File(path);

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    @DisplayName("Сохранение в файл")
    public void shouldSaveToFile() throws IOException {
        String path = "src/test/resources/history_for_data_test.csv";
        File file = new File(path);
        taskManager = new FileBackedTasksManager(file);
        SingleTask singleTask = new SingleTask("Another safe Task", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(1704056400000L), 707568400L);
        taskManager.addSingleTask(singleTask);
        EpicTask epicTask = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask);
        Subtask subtask = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 707568400L, epicTask.getId());
        taskManager.addNewSubTask(subtask);
        String actualResult = Files.readString(Paths.get(path));
        String expectedResult = "id,type,name,status,description,startTime,duration,epic\n" +
                "0,SINGLE,Another safe Task,NEW,Desc AST,2023-12-31T21:00:00Z,707568400\n" +
                "1,EPIC,First epic,NEW,Desc FE,1975-03-12T12:05:00Z,707568400\n" +
                "2,SUBTASK,First subtask for testing,NEW,Desc FSB,1975-03-12T12:05:00Z,707568400,1\n" +
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
        SingleTask singleTask = null;
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
        EpicTask epicTask = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask);

        String actualResult = Files.readString(Paths.get(path));
        String expectedResult = "id,type,name,status,description,startTime,duration,epic\n" +
                "0,EPIC,First epic,NEW,Desc FE,null,0\n" + "\n";
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
}
