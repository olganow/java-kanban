import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    TaskManager taskManager = Managers.getDefault();
    private EpicTask epicTaskOne;
    private EpicTask epicTaskSecond;
    private static final String VALIDATION_MESSAGE_FOR_EPIC_LIST = "The epic's subtask list is not empty";
    private static final String VALIDATION_MESSAGE_FOR_EPIC_STATUS = "The status of the Epic isn't correct";
    private Subtask subtaskOne;
    private Subtask subtaskSecond;

    @BeforeEach
    private void beforeEach() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        epicTaskSecond = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskSecond);

        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW, 1);
        taskManager.addNewSubTask(subtaskOne);
        subtaskSecond = new Subtask("Second subtask for testing", "Desc SSB", TaskStatus.NEW, 1);
        taskManager.addNewSubTask(subtaskSecond);
    }

    @Test
    @DisplayName("Пустой список подзадач")
    public void shouldBeEmptyListOfSubtasks() {
        assertTrue(epicTaskOne.getSubtaskIds().isEmpty(), VALIDATION_MESSAGE_FOR_EPIC_LIST);
    }

    @Test
    @DisplayName("Все подзадачи со статусом NEW")
    public void shouldHasStatusNewWhenAllSubtasksHaveStatusNew() {
        subtaskOne.setTaskStatus(TaskStatus.NEW);
        subtaskSecond.setTaskStatus(TaskStatus.NEW);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(TaskStatus.NEW, epicTaskSecond.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Все подзадачи со статусом DONE")
    public void shouldHasStatusDoneWhenAllSubtasksHaveStatusDone() {
        subtaskOne.setTaskStatus(TaskStatus.DONE);
        subtaskSecond.setTaskStatus(TaskStatus.DONE);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(TaskStatus.DONE, epicTaskSecond.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Подзадачи со статусами NEW и DONE")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusNewAndDone() {
        subtaskOne.setTaskStatus(TaskStatus.NEW);
        subtaskSecond.setTaskStatus(TaskStatus.DONE);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(TaskStatus.IN_PROGRESS, epicTaskSecond.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Подзадачи со статусами IN_PROGRESS")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusInProgress() {
        subtaskOne.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtaskSecond.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(TaskStatus.IN_PROGRESS, epicTaskSecond.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

}