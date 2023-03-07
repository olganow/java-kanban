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

    private EpicTask epicTask;
    private static final String VALIDATION_MESSAGE_FOR_EPIC_STATUS = "The status of the Epic isn't correct";

    @BeforeEach
    private void beforeEach()  {
        epicTask = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTask);
    }

    @Test
    @DisplayName("Пустой список подзадач")
    public void shouldBeEmptyListOfSubtasks() {
        assertTrue(epicTask.getSubtaskIds().isEmpty(),"The epic's subtask list is not empty ");
    }


    @Test
    @DisplayName("Все подзадачи со статусом NEW")
    public void shouldHasStatusNewWhenAllSubtasksHaveStatusNew() {
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.NEW, 0);
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.NEW, 0);
        taskManager.addNewSubTask(subtask2);
        assertEquals(TaskStatus.NEW, epicTask.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Все подзадачи со статусом DONE")
    public void shouldHasStatusDoneWhenAllSubtasksHaveStatusDone() {
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.DONE, 0);
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.DONE, 0);
        taskManager.addNewSubTask(subtask2);
        assertEquals(TaskStatus.DONE, epicTask.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Подзадачи со статусами NEW и DONE")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusNewAndDone() {
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.NEW, 0);
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.DONE, 0);
        taskManager.addNewSubTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

    @Test
    @DisplayName("Подзадачи со статусами IN_PROGRESS")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusInProgress() {
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.IN_PROGRESS, 0);
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.IN_PROGRESS, 0);
        taskManager.addNewSubTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getTaskStatus(), VALIDATION_MESSAGE_FOR_EPIC_STATUS);
    }

}