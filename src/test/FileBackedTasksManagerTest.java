import main.java.manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public final String path = "src/test/resources/history_data_test.csv";

    File file = new File(path);

    @BeforeEach
    public void beforeEach() {

        taskManager = new FileBackedTasksManager(file);

    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(Path.of(path));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}