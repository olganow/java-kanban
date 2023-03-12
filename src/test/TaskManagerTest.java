import main.java.description.TaskStatus;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    SingleTask singleTaskFirst;
    SingleTask singleTaskSecond;
    SingleTask singleTaskThird;

    private EpicTask epicTaskOne;
    private EpicTask epicTaskSecond;
    private Subtask subtaskOne;
    private Subtask subtaskSecond;


    @Test
    public void shouldCreateSingleTask() {
        singleTaskFirst = new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), 707568400L);
        taskManager.addSingleTask(singleTaskFirst);
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertEquals(List.of(singleTaskFirst), tasks);
    }

    @Test
    public void shouldCreateEpicTask() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertEquals(List.of(epicTaskOne), tasks);
    }

    @Test
    public void shouldCreateSubtask() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), 707568400L, 0);
        taskManager.addNewSubTask(subtaskOne);
        List<Task> tasks = taskManager.getAllSubtasks();
        assertEquals(List.of(subtaskOne), tasks);
    }

    @Test
    void shouldReturnNullWhenCreateSingleTaskNull() {
        SingleTask singleTask = null;
        taskManager.addSingleTask(singleTask);
        assertNull(singleTask);
    }

    @Test
    void shouldReturnNullWhenCreateEpicTaskNull() {
        EpicTask epicTask = null;
        taskManager.addEpicTask(epicTask);
        assertNull(epicTask);
    }

    @Test
    void shouldReturnNullWhenCreateSubtaskNull() {
        Subtask subtask = null;
        taskManager.addNewSubTask(subtask);
        assertNull(subtask);
    }

    @Test
    public void shouldAddValidIdWhenCreateSingleTaskWithInvalidId() {
        int id = -1;
        singleTaskFirst = new SingleTask(id, "First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), Instant.ofEpochMilli(1163857900000L));
        taskManager.addSingleTask(singleTaskFirst);
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertEquals(0, tasks.get(0).getId());
    }

    @Test
    public void shouldAddValidIdWhenCreateEpicTaskWithInvalidId() {
        int id = -1;
        epicTaskOne = new EpicTask(id, "First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertEquals(0, tasks.get(0).getId());
    }

    @Test
    public void shouldAddValidIdWhenCreateSubtaskWithInvalidId() {
        int id = -1;
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        subtaskOne = new Subtask(id, "First subtask for testing", "Desc FSB", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), Instant.ofEpochMilli(9163857900000L), 0);
        taskManager.addNewSubTask(subtaskOne);
        List<Task> tasks = taskManager.getAllSubtasks();
        assertEquals(1, tasks.get(0).getId());
    }

    @Test
    public void shouldDeleteALLSingleTask() {
        singleTaskFirst = new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), 707568400L);
        taskManager.addSingleTask(singleTaskFirst);
        taskManager.deleteAllSingleTask();
        assertNull(taskManager.getAllSingleTasks());

    }

    @Test
    public void shouldDeleteALLEpicTask() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllEpicTasks());
    }

    @Test
    public void shouldDeleteALLEpicTaskWithAllSubtasks() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    public void shouldDeleteALLSubtask() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), 707568400L, 0);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    public void shouldDeleteSingleTaskById() {
        singleTaskFirst = new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 7900000L);
        taskManager.addSingleTask(singleTaskFirst);
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.deleteById(0);
        assertNull(taskManager.getAllSingleTasks());
    }

    @Test
    public void shouldDeleteEpicTaskById() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.deleteById(0);
        assertNull(taskManager.getAllEpicTasks());
    }

    @Test
    public void shouldDeleteEpicTaskWithSubtaskById() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW, Instant.ofEpochMilli(163857900000L), 163857900000L, 0);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.deleteById(0);
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    public void shouldDeleteSubtaskById() {
        epicTaskOne = new EpicTask("First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskOne);
        subtaskOne = new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 9163857900000L, 0);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.deleteById(1);
        assertNull(taskManager.getAllSubtasks());
    }
}

/*

    void deleteById(int id);

    void updateSingleTask(SingleTask singleTask);

    void updateEpicTask(EpicTask epicTask);

    void updateSubtask(Subtask subtask);

    List<Task> getAllSingleTasks();

    List<Task> getAllEpicTasks();

    List<Task> getAllSubtasks();

    List<Subtask> getSubTasksByEpicId(int id);

    Task getTaskById(int id);

    Set<Task> getPrioritizedTasks();

    */


