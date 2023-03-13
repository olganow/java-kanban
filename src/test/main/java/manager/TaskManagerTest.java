package main.java.manager;

import main.java.description.TaskStatus;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected EpicTask createEpicTask() {

        return new EpicTask("First epic for testing", "Desc FE for testing ");
    }

    protected Subtask createSubtask(EpicTask epic) {
        return new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 707568400L, epic.getId());
    }


    protected SingleTask createSingleTask() {
        return new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 57900000L);
    }


    @Test
    @DisplayName("Создание задачи")
    public void shouldCreateSingleTask() {
        SingleTask singleTask = createSingleTask();
        taskManager.addSingleTask(singleTask);
        String expectedResult = "[" + singleTask.toString() + "]";
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Создание эпика")
    public void shouldCreateEpicTask() {
        EpicTask epicTask = createEpicTask();
        taskManager.addEpicTask(epicTask);
        String expectedResult = "[" + epicTask.toString() + "]";
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Создание подзадачи")
    public void shouldCreateSubtask() {
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);
        String expectedResult = "[" + subtask.toString() + "]";
        List<Task> tasks = taskManager.getAllSubtasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Создание Null задачи")
    void shouldReturnNullWhenCreateSingleTaskNull() {
        SingleTask singleTask = null;
        taskManager.addSingleTask(singleTask);
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertNull(tasks);
    }

    @Test
    @DisplayName("Создание Null эпика")
    void shouldReturnNullWhenCreateEpicTaskNull() {
        EpicTask epicTask = null;
        taskManager.addEpicTask(epicTask);
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertNull(tasks);
    }

    @Test
    @DisplayName("Создание Null подзадачи")
    void shouldReturnNullWhenCreateSubtaskNull() {
        Subtask subtask = null;
        taskManager.addNewSubTask(subtask);
        List<Task> tasks = taskManager.getAllSubtasks();
        assertNull(tasks);
    }


    @Test
    @DisplayName("Создание задачи c невалидным id")
    public void shouldAddValidIdWhenCreateSingleTaskWithInvalidId() {
        int id = -100;
        SingleTask singleTaskInvalid = new SingleTask(id, "First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), Instant.ofEpochMilli(1163857900000L));
        taskManager.addSingleTask(singleTaskInvalid);
        int expectedResult = 0;
        assertEquals(expectedResult, singleTaskInvalid.getId());
    }

    @Test
    @DisplayName("Создание эпика c невалидным id")
    public void shouldAddValidIdWhenCreateEpicTaskWithInvalidId() {
        int id = -100;
        EpicTask epicTaskInvalid = new EpicTask(id, "First epic for testing", "Desc FE for testing ");
        taskManager.addEpicTask(epicTaskInvalid);
        int expectedResult = 0;
        assertEquals(expectedResult, epicTaskInvalid.getId());
    }

    @Test
    @DisplayName("Создание подзадачи c невалидным id")
    public void shouldAddValidIdWhenCreateSubtaskWithInvalidId() {
        int id = -100;
        EpicTask epicTask = createEpicTask();
        taskManager.addEpicTask(epicTask);
        Subtask subtaskInvalid = new Subtask(id, "First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), Instant.ofEpochMilli(9163857900000L), epicTask.getId());
        taskManager.addNewSubTask(subtaskInvalid);
        int expectedResult = 1;
        assertEquals(expectedResult, subtaskInvalid.getId());
    }


    @Test
    @DisplayName("Удаление всех задач")
    public void shouldDeleteALLSingleTask() {
        SingleTask singleTask = createSingleTask();
        taskManager.addSingleTask(singleTask);
        taskManager.deleteAllSingleTask();
        assertNull(taskManager.getAllSingleTasks());

    }

    @Test
    @DisplayName("Удаление всех эпик без подзадач")
    public void shouldDeleteALLEpicTask() {
        EpicTask epicTask = createEpicTask();
        taskManager.addEpicTask(epicTask);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllEpicTasks());
    }

    @Test
    @DisplayName("Удаление всех эпиков с подзадачами")
    public void shouldDeleteALLEpicTaskWithAllSubtasks() {
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Удаление всех подзадач")
    public void shouldDeleteALLSubtask() {
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);
        taskManager.deleteAllEpicTask();
        assertNull(taskManager.getAllSubtasks());
    }


    @Test
    @DisplayName("Удаление задачи по ID")
    public void shouldDeleteSingleTaskById() {
        SingleTask singleTask = createSingleTask();
        taskManager.addSingleTask(singleTask);
        EpicTask epicTask = createEpicTask();

        taskManager.addEpicTask(epicTask);
        taskManager.deleteById(singleTask.getId());
        assertNull(taskManager.getAllSingleTasks());
    }

    @Test
    @DisplayName("Удаление эпика без сабтасок по ID")
    public void shouldDeleteEpicTaskById() {
        SingleTask singleTask = createSingleTask();
        EpicTask epicTask = createEpicTask();
        taskManager.addSingleTask(singleTask);
        taskManager.addEpicTask(epicTask);
        taskManager.deleteById(epicTask.getId());
        assertNull(taskManager.getAllEpicTasks());
    }

    @Test
    @DisplayName("Удаление эпика с сабтасками по ID")
    public void shouldDeleteEpicTaskWithSubtaskById() {
        SingleTask singleTask = createSingleTask();
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
        taskManager.addSingleTask(singleTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);
        taskManager.deleteById(epicTask.getId());
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Удаление сабтаски по ID")
    public void shouldDeleteSubtaskById() {
        EpicTask epicTask = createEpicTask();
        taskManager.addEpicTask(epicTask);
        Subtask subtask = createSubtask(epicTask);
        taskManager.addNewSubTask(subtask);
        taskManager.deleteById(subtask.getId());
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Удаление с некорректным ID")
    public void shouldValidateDeleteWhenDeleteByInvalidId() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            taskManager.addEpicTask(epicTask);
            Subtask subtask = createSubtask(epicTask);
            taskManager.addNewSubTask(subtask);
            int invalidId = 100;
            taskManager.deleteById(invalidId);
        });
        Assertions.assertEquals("Такого id нет", exception.getMessage());
    }

    @Test
    @DisplayName("Удаление по Id из пустого списка")
    public void shouldValidateDeleteWhenDeleteFromEmptyList() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            int invalidId = 100;
            taskManager.deleteById(invalidId);
        });
        Assertions.assertEquals("Такого id нет", exception.getMessage());
    }

    @Test
    @DisplayName("Редактирование задачи")
    public void shouldUpdateSingleTask() {
        SingleTask singleTask = createSingleTask();
        taskManager.addSingleTask(singleTask);
        singleTask.setDuration(707568400L);
        singleTask.setStartTime(Instant.ofEpochMilli(9162714000000L));
        singleTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        String expectedResult = "[" + singleTask + "]";
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Редактирование эпика")
    public void shouldUpdateEpicTaskWithSubtack() {
        EpicTask epicTask = createEpicTask();
        taskManager.addEpicTask(epicTask);
        Subtask subtask = createSubtask(epicTask);
        taskManager.addNewSubTask(subtask);
        String expectedResult = "[" + epicTask + "]";
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Редактирование подзадачи")
    public void shouldUpdateSubtask() {
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtask);
        subtask.setDuration(707568400L);
        subtask.setStartTime(Instant.ofEpochMilli(9162714000000L));
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        String expectedResult = "[" + subtask + "]";
        List<Task> tasks = taskManager.getAllSubtasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Редактирование Null задачи")
    public void shouldNotUpdateSingleTaskIfNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            SingleTask singleTask = createSingleTask();
            taskManager.addSingleTask(singleTask);
            taskManager.updateSingleTask(null);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Редактирование Null эпика")
    public void shouldNotUpdateEpicIfNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            taskManager.addEpicTask(epicTask);
            taskManager.updateEpicTask(null);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Редактирование Null подзадачи")
    public void shouldNotUpdateSubtaskIfNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            Subtask subtask = createSubtask(epicTask);
            taskManager.addEpicTask(epicTask);
            taskManager.addNewSubTask(subtask);
            taskManager.updateSubtask(null);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Редактирование задачи в Null листе")
    public void shouldNotUpdateSingleTaskIfListIsNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            SingleTask singleTask = createSingleTask();
            taskManager.updateSingleTask(singleTask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }


    @Test
    @DisplayName("Редактирование эпика в Null листе")
    public void shouldNotUpdateEpicIfListIsNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            taskManager.updateEpicTask(epicTask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Редактирование подзадачи в Null листе")
    public void shouldNotUpdateSubtaskIfListIsNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            Subtask subtask = createSubtask(epicTask);
            taskManager.addEpicTask(epicTask);
            taskManager.updateSubtask(subtask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

}

/*


    List<Task> getAllSingleTasks();
    List<Task> getAllEpicTasks();
    List<Task> getAllSubtasks();
    List<Subtask> getSubTasksByEpicId(int id);
    Task getTaskById(int id);
    Set<Task> getPrioritizedTasks();
    */
