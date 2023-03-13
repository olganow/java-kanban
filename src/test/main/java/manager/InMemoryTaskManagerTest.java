package main.java.manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;



class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }
    @AfterEach
    public void afterEach() {
        taskManager.deleteAllTask();
    }
}