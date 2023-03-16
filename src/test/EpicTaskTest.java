import main.java.description.TaskStatus;
import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    TaskManager taskManager = Managers.getDefault();
    private EpicTask epicTaskOne;
    private EpicTask epicTaskSecond;
    private static final String VALIDATION_MESSAGE_FOR_EPIC_LIST = "The epic's subtask list is not empty";
    private static final String VALIDATION_MESSAGE_FOR_EPIC_STATUS = "The status of the Epic isn't correct";
    private Subtask subtaskOne;
    private Subtask subtaskSecond;
    private Subtask subtaskThird;
    private Subtask subtaskWithNullTime;

    @BeforeEach
    private void beforeEach() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        epicTaskSecond = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskSecond);

        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 707568400L, epicTaskSecond.getId());
        taskManager.addNewSubTask(subtaskOne);
        subtaskSecond = new Subtask("Second subtask for testing", "Desc SSB", TaskStatus.NEW,
                Instant.ofEpochMilli(99173857900000L), 707568400L, epicTaskSecond.getId());
        taskManager.addNewSubTask(subtaskSecond);

        subtaskThird = new Subtask("Third subtask for testing", "Desc SSB", TaskStatus.NEW,
                Instant.ofEpochMilli(900000L), 400L, epicTaskOne.getId());

        subtaskWithNullTime = new Subtask("Null Time subtask for testing", "Desc SSB", TaskStatus.NEW,
                null, 0L, epicTaskSecond.getId());
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


    @Test
    @DisplayName("Установка начального времени для эпика, когда нет подзадач")
    public void shouldSetEpicStartTimeWithoutSubtask() {
        assertNull(epicTaskOne.getStartTime());
    }

    @Test
    @DisplayName("Установка конечного времени для эпика, когда нет подзадач")
    public void shouldSetEpicEndTimeWithoutSubtask() {
        assertNull(epicTaskOne.getEndTime());
    }

    @Test
    @DisplayName("Установка начального времени для эпика, когда одна подзадача ")
    public void shouldSetEpicStartTimeWhenOneSubtask() {
        taskManager.addNewSubTask(subtaskThird);
        taskManager.updateSubtask(subtaskOne);
        //   taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(subtaskThird.getStartTime(), epicTaskOne.getStartTime());
    }

    @Test
    @DisplayName("Установка начального времени для эпика, когда одна подзадача ")
    public void shouldSetEpicEndTimeWhenOneSubtask() {
        taskManager.addNewSubTask(subtaskThird);
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(subtaskThird.getEndTime(), epicTaskOne.getEndTime());
    }

    @Test
    @DisplayName("Проверка длительности для эпика, когда одна подзадача ")
    public void shouldSetEpicDurationTimeWhenOneSubtask() {
        taskManager.addNewSubTask(subtaskThird);
        taskManager.updateSubtask(subtaskOne);
        //   taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(subtaskThird.getDuration(), epicTaskOne.getDuration());
    }

    @Test
    @DisplayName("Установка начального времени для эпика, когда у подзадач начальное время установлено")
    public void shouldSetEpicStartTimeWhenAllSubtasksHaveStartTime() {
        subtaskOne.setStartTime(Instant.MIN);
        taskManager.updateSubtask(subtaskOne);
        subtaskSecond.setStartTime(Instant.ofEpochMilli(99173857900000L));
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(Instant.MIN, epicTaskSecond.getStartTime());
    }

    @Test
    @DisplayName("Установка конечного времени для эпика, когда у подзадач конечное время установлено")
    public void shouldSetEpicEndTimeWhenAllSubtasksHaveEndTime() {
        subtaskOne.setDuration(348053485L);
        taskManager.updateSubtask(subtaskOne);
        subtaskSecond.setStartTime(Instant.MAX);
        subtaskSecond.setDuration(0);
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(Instant.MAX, epicTaskSecond.getEndTime());
    }

    @Test
    @DisplayName("Установка начального времени для эпика, когда у одной из подзадачи начальное время null")
    public void shouldSetEpicStartTimeNullWhenAllSubtasksHaveStartTimeNull() {
        taskManager.addNewSubTask(subtaskWithNullTime);
        subtaskSecond.setStartTime(Instant.MIN);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(Instant.MIN, epicTaskSecond.getStartTime());
    }

    @Test
    @DisplayName("Установка конечного времени для эпика, когда у одной из подзадачи конечное время null")
    public void shouldSetEpicEndTimeWhenAllSubtasksHaveEndTimeNull() {
        taskManager.addNewSubTask(subtaskWithNullTime);
        subtaskSecond.setStartTime(Instant.MAX);
        subtaskSecond.setDuration(0);
        taskManager.updateEpicTask(epicTaskSecond);
        assertEquals(Instant.MAX, epicTaskSecond.getEndTime());
    }

}