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
    private Subtask subtask;
    private Subtask subtask1;
    private static final String VALIDATION_MESSAGE = "The status of the Epic isn't correct";

    @BeforeEach
    private void beforeEach() {

        //EpicTask epicTask = new EpicTask("First epic", "Desc FE");
      //  taskManager.addEpicTask(epicTask);
        //Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.NEW, 0);
        //taskManager.addNewSubTask(subtask1);
        //Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.NEW, 0);
       // taskManager.addNewSubTask(subtask2);

    }

    @Test
    @DisplayName("Пустой список подзадач")
    public void shouldBeEmptyListOfSubtasks() {
       EpicTask epicTask = new EpicTask("First epic", "Desc FE");
       taskManager.addEpicTask(epicTask);
        System.out.println(epicTask.getSubtaskIds());
        assertTrue(epicTask.getSubtaskIds().isEmpty());
    }


    @Test
    @DisplayName("Все подзадачи со статусом NEW")
    public void shouldHasStatusNewWhenAllSubtasksHaveStatusNew() {
        EpicTask epicTask = new EpicTask("First epic", "Desc FE");
        taskManager.addEpicTask(epicTask);
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.NEW, 0);
        taskManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.NEW, 0);
        taskManager.addNewSubTask(subtask2);
        System.out.println(epicTask.getTaskStatus());
        assertEquals(TaskStatus.NEW, epicTask.getTaskStatus(), VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Все подзадачи со статусом DONE")
    public void shouldHasStatusDoneWhenAllSubtasksHaveStatusDone() {

        assertEquals(TaskStatus.DONE, epicTask.getTaskStatus(), VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Подзадачи со статусами NEW и DONE")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusNewAndDone() {

        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getTaskStatus(), VALIDATION_MESSAGE);
    }

    @Test
    @DisplayName("Подзадачи со статусами NEW и DONE")
    public void shouldHasStatusInProgressWhenAllSubtasksHaveStatusInProgress() {

        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getTaskStatus(), VALIDATION_MESSAGE);
    }

}