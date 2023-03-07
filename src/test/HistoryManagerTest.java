import main.java.description.TaskStatus;
import main.java.manager.HistoryManager;
import main.java.manager.Managers;
import main.java.task.SingleTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {
    HistoryManager historyManager;
    SingleTask singleTaskFirst;
    SingleTask singleTaskSecond;
    SingleTask singleTaskThird;

    @BeforeEach
    private void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        singleTaskFirst = new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW);
        singleTaskFirst.setId(1);

        singleTaskSecond = new SingleTask("Second Single Task for testing", "Desc SST", TaskStatus.NEW);
        singleTaskSecond.setId(2);

        singleTaskThird = new SingleTask("Third Single Task for testing", "Desc SST", TaskStatus.NEW);
        singleTaskThird.setId(3);
    }

    @Test
    @DisplayName("Пустая история задач")
    public void shouldBeEmptyListOfSubtasks() {
        assertEquals(0,historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Дублированиe")
    public void shouldNotHaveDoubles() {
        historyManager.add(singleTaskFirst);
        historyManager.add(singleTaskFirst);
        assertEquals(1,historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Удаление из началa истории")
    public void shouldBeDeletedFromTheStart() {
        historyManager.add(singleTaskFirst);
        historyManager.add(singleTaskSecond);
        historyManager.add(singleTaskThird);
        historyManager.remove(singleTaskFirst.getId());
        assertEquals(2,historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Удаление из середины истории")
    public void shouldBeDeletedFromTheMiddle() {
        historyManager.add(singleTaskFirst);
        historyManager.add(singleTaskSecond);
        historyManager.add(singleTaskThird);
        historyManager.remove(singleTaskSecond.getId());
        assertEquals(2,historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Удаление из конца истории")
    public void shouldBeDeletedFromTheEnd() {
        historyManager.add(singleTaskFirst);
        historyManager.add(singleTaskSecond);
        historyManager.add(singleTaskThird);
        historyManager.remove(singleTaskThird.getId());
        assertEquals(2,historyManager.getHistory().size());
    }


}