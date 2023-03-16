import main.java.description.TaskStatus;
import main.java.manager.ManagerSaveException;
import main.java.manager.TaskManager;
import main.java.task.EpicTask;
import main.java.task.SingleTask;
import main.java.task.Subtask;
import main.java.task.Task;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected EpicTask createEpicTask() {

        return new EpicTask("First epic for testing", "Desc FE for testing ");
    }

    protected Subtask createSubtask(EpicTask epic) {
        return new Subtask("First subtask for testing", "Desc FSB", TaskStatus.NEW,
                Instant.ofEpochMilli(57900000L), 6800L, epic.getId());
    }


    protected SingleTask createSingleTask() {
        return new SingleTask("First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(16355857900000L), 579000L);
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
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            SingleTask singleTask = null;
            taskManager.addSingleTask(singleTask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Создание Null эпика")
    void shouldReturnNullWhenCreateEpicTaskNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = null;
            taskManager.addEpicTask(epicTask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Создание Null подзадачи")
    void shouldReturnNullWhenCreateSubtaskNull() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            Subtask subtask = null;
            taskManager.addNewSubTask(subtask);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }


    @Test
    @DisplayName("Создание задачи c невалидным id")
    public void shouldAddValidIdWhenCreateSingleTaskWithInvalidId() {
        int id = -100;
        SingleTask singleTaskInvalid = new SingleTask(id, "First Single Task for testing", "Desc SST", TaskStatus.NEW,
                Instant.ofEpochMilli(163857900000L), 57900000L);
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
                Instant.ofEpochMilli(163857900000L), 163857900000L, epicTask.getId());
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
        EpicTask epicTask = createEpicTask();
        Subtask subtask = createSubtask(epicTask);
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

    @Test
    @DisplayName("Получение всех задач")
    public void shouldGetAllSingleTasks() {
        SingleTask singleTaskOne = createSingleTask();
        taskManager.addSingleTask(singleTaskOne);
        SingleTask singleTaskSecond = createSingleTask();
        singleTaskSecond.setStartTime(Instant.now());
        taskManager.addSingleTask(singleTaskSecond);
        List<Task> expectedSingleTaskList = new ArrayList<>();
        expectedSingleTaskList.add(singleTaskOne);
        expectedSingleTaskList.add(singleTaskSecond);
        String expectedResult = expectedSingleTaskList.toString();
        List<Task> tasks = taskManager.getAllSingleTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Получение всех эпиков")
    public void shouldGetAlleEpicTasks() {
        EpicTask epicTaskOne = createEpicTask();
        taskManager.addEpicTask(epicTaskOne);
        Subtask subtask = createSubtask(epicTaskOne);
        taskManager.addNewSubTask(subtask);
        EpicTask epicTaskSecond = createEpicTask();
        taskManager.addEpicTask(epicTaskSecond);
        List<Task> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(epicTaskOne);
        expectedEpicList.add(epicTaskSecond);
        String expectedResult = expectedEpicList.toString();
        List<Task> tasks = taskManager.getAllEpicTasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Получение всех подзадач")
    public void shouldGetAllSubtasks() {
        EpicTask epicTask = createEpicTask();
        Subtask subtaskOne = createSubtask(epicTask);
        Subtask subtaskSecond = createSubtask(epicTask);
        subtaskSecond.setStartTime(Instant.now());
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        List<Task> expectedSubtaskList = new ArrayList<>();
        expectedSubtaskList.add(subtaskOne);
        expectedSubtaskList.add(subtaskSecond);
        String expectedResult = expectedSubtaskList.toString();
        List<Task> tasks = taskManager.getAllSubtasks();
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Получение всех задач из пустого листа с задачами")
    public void shouldNotGetSingleTaskFromEmptySingleTasklist() {
        assertNull(taskManager.getAllSingleTasks());
    }

    @Test
    @DisplayName("Получение всех задач из пустого листа с эпиками")
    public void shouldNotGetEpicTaskFromEmptyEpicTasklist() {
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Получение всех задач из пустого листа с подзадачами")
    public void shouldNotGetSubtaskFromEmptySubtaskTasklist() {
        assertNull(taskManager.getAllSubtasks());
    }

    @Test
    @DisplayName("Получение всех подзадач по эпик Id")
    public void shouldGetSubTasksByEpicId() {
        EpicTask epicTask = createEpicTask();
        Subtask subtaskOne = createSubtask(epicTask);
        Subtask subtaskSecond = createSubtask(epicTask);
        subtaskSecond.setStartTime(Instant.now());
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        taskManager.getSubTasksByEpicId(epicTask.getId());
        List<Task> expectedSubtaskList = new ArrayList<>();
        expectedSubtaskList.add(subtaskOne);
        expectedSubtaskList.add(subtaskSecond);
        String expectedResult = expectedSubtaskList.toString();
        List<Subtask> tasks = taskManager.getSubTasksByEpicId(epicTask.getId());
        assertEquals(expectedResult, tasks.toString());
    }

    @Test
    @DisplayName("Получение всех подзадач по эпик Id из пустого листа")
    public void shouldNotGetSubTasksByEpicIdIfSubtaskListIsEmpty() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            taskManager.getSubTasksByEpicId(epicTask.getId());
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Получение всех подзадач по невалидному эпик Id")
    public void shouldNotGetSubTasksByEpicIdWhenSubtaskIdInvalid() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            int invalidId = epicTask.getId() + 100;
            taskManager.getSubTasksByEpicId(invalidId);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Получение задачи по Id")
    public void shouldGetSingleTasksById() {
        SingleTask singleTaskOne = createSingleTask();
        taskManager.addSingleTask(singleTaskOne);
        SingleTask singleTaskSecond = createSingleTask();
        singleTaskSecond.setStartTime(Instant.now());
        taskManager.addSingleTask(singleTaskSecond);
        String task = taskManager.getTaskById(singleTaskSecond.getId()).toString();
        assertEquals(singleTaskSecond.toString(), task);
    }

    @Test
    @DisplayName("Получение эпика по Id")
    public void shouldGetEpicTasksById() {
        EpicTask epicTaskOne = createEpicTask();
        taskManager.addEpicTask(epicTaskOne);
        EpicTask epicTaskSecond = createEpicTask();
        taskManager.addEpicTask(epicTaskSecond);
        String task = taskManager.getTaskById(epicTaskSecond.getId()).toString();
        assertEquals(epicTaskSecond.toString(), task);
    }

    @Test
    @DisplayName("Получение подзадачи по Id")
    public void shouldGetSubtaskById() {
        EpicTask epicTask = createEpicTask();
        Subtask subtaskOne = createSubtask(epicTask);
        Subtask subtaskSecond = createSubtask(epicTask);
        subtaskSecond.setStartTime(Instant.now());
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        String task = taskManager.getTaskById(subtaskSecond.getId()).toString();
        assertEquals(subtaskSecond.toString(), task);
    }

    @Test
    @DisplayName("Получение задачи по Id из пустого листа")
    public void shouldNotGetTasksIfSingleTaskListIsEmpty() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            int invalidId = 123;
            taskManager.getTaskById(invalidId);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Получение задачи по невалидному эпик Id")
    public void shouldNotGetTasksByInvalidId() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            taskManager.addEpicTask(epicTask);
            int invalidId = 123;
            taskManager.getTaskById(invalidId);
        });
        Assertions.assertEquals("Такой задачи нет", exception.getMessage());
    }

    @Test
    @DisplayName("Приоритизация по времени")
    public void shouldGetPrioritizedTaskList() {
        EpicTask epicTask = createEpicTask();
        Subtask subtaskOne = createSubtask(epicTask);
        Subtask subtaskSecond = createSubtask(epicTask);
        subtaskSecond.setStartTime(Instant.now());
        taskManager.addEpicTask(epicTask);
        taskManager.addNewSubTask(subtaskOne);
        taskManager.addNewSubTask(subtaskSecond);
        SingleTask singleTaskOne = createSingleTask();
        singleTaskOne.setStartTime(Instant.MIN);
        taskManager.addSingleTask(singleTaskOne);
        Set<Task> expectedSubtaskList = new TreeSet<>((o1, o2) -> {
            if (o2.getStartTime() == null) {
                return -1;
            } else if (o1.getStartTime() == null)
                return 1;
            else if (o1.getStartTime() == o2.getStartTime()) {
                return 0;
            } else
                return o1.getStartTime().compareTo(o2.getStartTime());
        });

        expectedSubtaskList.add(subtaskOne);
        expectedSubtaskList.add(subtaskSecond);
        expectedSubtaskList.add(singleTaskOne);
        String expectedResult = expectedSubtaskList.toString();
        String task = taskManager.getPrioritizedTasks().toString();

        assertEquals(expectedResult, task);
    }

    @Test
    @DisplayName("Невалидная приоритизация по времени")
    public void shouldNotGetPrioritizedTaskListIfTasksHaveSimilarTime() {
        ManagerSaveException exception = Assertions.assertThrows(ManagerSaveException.class, () -> {
            EpicTask epicTask = createEpicTask();
            Subtask subtaskOne = createSubtask(epicTask);
            Subtask subtaskSecond = createSubtask(epicTask);
            subtaskOne.setStartTime(Instant.ofEpochMilli(57900000L));
            subtaskSecond.setStartTime(Instant.ofEpochMilli(57900000L));
            taskManager.addEpicTask(epicTask);
            taskManager.addNewSubTask(subtaskOne);
            taskManager.addNewSubTask(subtaskSecond);
            SingleTask singleTaskOne = createSingleTask();
            taskManager.addSingleTask(singleTaskOne);
            Set<Task> expectedSubtaskList = new TreeSet<>((o1, o2) -> {
                if (o2.getStartTime() == null) {
                    return -1;
                } else if (o1.getStartTime() == null)
                    return 1;
                else if (o1.getStartTime() == o2.getStartTime()) {
                    return 0;
                } else
                    return o1.getStartTime().compareTo(o2.getStartTime());
            });

            expectedSubtaskList.add(subtaskOne);
            expectedSubtaskList.add(subtaskSecond);
            expectedSubtaskList.add(singleTaskOne);
            String expectedResult = expectedSubtaskList.toString();
            String task = taskManager.getPrioritizedTasks().toString();

            assertEquals(expectedResult, task);
        });
        String expectedResult = "TimeIntersections: the task with a name \"First subtask for testing\" " +
                "with id = 2 with start time: 1970-01-01T16:05:00Z with end time: 1970-01-01T17:58:20Z and the task" +
                " with id = 1 with start time: 1970-01-01T16:05:00Z and  with end time: 1970-01-01T17:58:20Z";
        Assertions.assertEquals(expectedResult, exception.getMessage());
    }

}
