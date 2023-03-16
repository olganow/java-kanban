package main.java.manager;

import main.java.description.TaskType;
import main.java.task.SingleTask;
import main.java.task.Task;
import main.java.description.TaskStatus;
import main.java.task.EpicTask;
import main.java.task.Subtask;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final TaskIdGenerator taskIdGenerator;
    protected final Map<Integer, SingleTask> singleTaskById;
    protected final Map<Integer, EpicTask> epicTaskById;
    protected final Map<Integer, Subtask> subtaskById;
    protected final HistoryManager historyManager;
    protected Set<Task> sortedTasks;

    public InMemoryTaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTaskById = new HashMap<>();
        this.epicTaskById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.sortedTasks = new TreeSet<>((o1, o2) -> {
            //   if (o1.getStartTime() == null && o2.getStartTime() == null ){
            if (o2.getStartTime() == null) {
                return -1;
            } else if (o1.getStartTime() == null)
                return 1;
            else if (o1.getStartTime() == o2.getStartTime()) {
                return 0;
            } else
                return o1.getStartTime().compareTo(o2.getStartTime());
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    //option 1: how to save object with generated id in hashmap
    public void addSingleTask(SingleTask singleTask) {
        try {
            // 1: generate new id and save it to the task
            singleTask.setId(taskIdGenerator.getNextFreedI());
            // 2: save task
            validateTaskTimeIntersections(singleTask);
            singleTaskById.put(singleTask.getId(), singleTask);
            sortedTasks.add(singleTask);
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        try {
            // 1: generate new id and save it to the task
            epicTask.setId(taskIdGenerator.getNextFreedI());
            // 2: save task
            epicTaskById.put(epicTask.getId(), epicTask);
            // 3: Epic Status updated
            setEpicStatus(epicTask.getId());
            setEpicStartAndEndTime(epicTask.getId());
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void addNewSubTask(Subtask subtask) {
        try {
            // 1: generate new id and save it to the task
            subtask.setId(taskIdGenerator.getNextFreedI());
            // 2: save task
            validateTaskTimeIntersections(subtask);
            EpicTask epicTask = epicTaskById.get(subtask.getEpicId());
            Integer id = subtask.getId();
            epicTask.getSubtaskIds().add(id);
            subtaskById.put(id, subtask);
            sortedTasks.add(subtask);
            // 3: Epic and StartAndEndTime Status updated
            setEpicStatus(epicTask.getId());
            setEpicStartAndEndTime(epicTask.getId());
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void deleteAllSingleTask() {
        for (int id : singleTaskById.keySet()) {
            historyManager.remove(id);
        }
        deleteAllTaskTypeInSortedList(TaskType.SINGLE);
        singleTaskById.clear();

    }

    public void deleteAllEpicTask() {
        for (int id : epicTaskById.keySet()) {
            historyManager.remove(id);
        }
        epicTaskById.clear();

        for (int id : subtaskById.keySet()) {
            historyManager.remove(id);
        }
        deleteAllTaskTypeInSortedList(TaskType.SUBTASK);
        subtaskById.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (int id : subtaskById.keySet()) {
            historyManager.remove(id);
        }
        deleteAllTaskTypeInSortedList(TaskType.SUBTASK);
        subtaskById.clear();
        for (EpicTask epic : epicTaskById.values()) {
            epic.getSubtaskIds().clear();
            setEpicStatus(epic.getId());
            setEpicStartAndEndTime(epic.getId());
        }
    }

    public void deleteAllTaskTypeInSortedList(TaskType taskType) {
        Set<Task> sortedTasksForDelete = new TreeSet<>(sortedTasks);
        for (Task task : sortedTasksForDelete) {
            if (task.getTaskType() == taskType) {
                sortedTasks.remove(task);
            }
        }
    }

    @Override
    public void deleteAllTask() {
        deleteAllSingleTask();
        deleteAllEpicTask();
        deleteAllSubtask();
    }

    @Override
    public void deleteById(int id) {
        if (singleTaskById.containsKey(id)) {
            sortedTasks.remove(singleTaskById.get(id));
            singleTaskById.remove(id);
            System.out.println("Задача с id=" + id + " удалена");
            historyManager.remove(id);

        } else if (epicTaskById.containsKey(id)) {
            EpicTask epicTask = epicTaskById.remove(id);
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                sortedTasks.remove(subtaskById.get(subtaskId));
                subtaskById.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
            System.out.println("Задача с id=" + id + ", и ее сабтаски удалены");

        } else if (subtaskById.containsKey(id)) {
            sortedTasks.remove(subtaskById.get(id));
            Subtask subtask = subtaskById.remove(id);
            int epicId = subtask.getEpicId();
            historyManager.remove(id);
            System.out.println("Подзадача с id=" + id + " удалена");

            //Epic Status and Epic List updated
            epicTaskById.get(epicId).getSubtaskIds().remove((Integer.valueOf(id)));
            setEpicStatus(epicId);
            setEpicStartAndEndTime(epicId);
        } else throw new ManagerSaveException("Такого id нет");
    }

    @Override
    public void updateSingleTask(SingleTask singleTask) {
        if (singleTaskById.containsValue(singleTask)) {
            validateTaskTimeIntersections(singleTask);
            singleTaskById.put(singleTask.getId(), singleTask);
            sortedTasks.add(singleTask);
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        if (epicTaskById.containsValue(epicTask)) {
            epicTaskById.put(epicTask.getId(), epicTask);
            //Epic Status updated
            setEpicStatus(epicTask.getId());
            setEpicStartAndEndTime(epicTask.getId());
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskById.containsValue(subtask)) {
            validateTaskTimeIntersections(subtask);
            subtaskById.put(subtask.getId(), subtask);
            sortedTasks.add(subtask);
            //Epic Status updated
            setEpicStatus(subtask.getEpicId());
            setEpicStartAndEndTime(subtask.getEpicId());
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public List<Task> getAllSingleTasks() {
        List<Task> tasks = new ArrayList<>(this.singleTaskById.values());
        if (tasks.isEmpty()) {
            return null;
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllEpicTasks() {
        List<Task> tasks = new ArrayList<>(this.epicTaskById.values());
        if (tasks.isEmpty()) {
            return null;
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllSubtasks() {
        List<Task> tasks = new ArrayList<>(this.subtaskById.values());
        if (tasks.isEmpty()) {
            return null;
        } else {
            return tasks;
        }
    }

    @Override
    public List<Subtask> getSubTasksByEpicId(int id) {
        if (epicTaskById.containsKey(id)) {
            EpicTask epic = epicTaskById.get(id);
            List<Subtask> subtasks = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.add(subtaskById.get(subtaskId));
            }
            return subtasks;
        } else {
            throw new ManagerSaveException("Такой задачи нет");
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (singleTaskById.containsKey(id)) {
            SingleTask task = singleTaskById.get(id);
            historyManager.add(task);
            return task;
        } else if (epicTaskById.containsKey(id)) {
            EpicTask task = epicTaskById.get(id);
            historyManager.add(task);
            return task;
        } else if (subtaskById.containsKey(id)) {
            Subtask task = subtaskById.get(id);
            historyManager.add(task);
            return task;
        } else {
            throw new ManagerSaveException("Такой задачи нет");
        }
    }

    private void validateTaskTimeIntersections(Task newTask) {
        if (!sortedTasks.isEmpty()) {
            if (newTask.getStartTime() != null || newTask.getEndTime() != null) {
                for (Task taskPriority : sortedTasks) {
                    if (newTask.getId() == taskPriority.getId() || taskPriority.getStartTime() == null ||
                            taskPriority.getEndTime() == null) {
                        continue;
                    }
                    if (!((newTask.getEndTime().isBefore(taskPriority.getStartTime()) &&
                            taskPriority.getStartTime() != null)
                            ||
                            (newTask.getStartTime().isAfter(taskPriority.getEndTime()) &&
                                    taskPriority.getEndTime() != null)
                            ||
                            newTask.getEndTime() == taskPriority.getStartTime()
                            ||
                            newTask.getEndTime() == taskPriority.getStartTime()
                            ||
                            (newTask.getStartTime().isBefore(taskPriority.getStartTime()) &&
                                    taskPriority.getStartTime() != null &&
                                    newTask.getEndTime().isAfter(taskPriority.getEndTime())
                                    && taskPriority.getEndTime() != null
                            )
                    )) {
                        throw new ManagerSaveException (
                                "TimeIntersections: the task with a name \"" + newTask.getName() + "\" with id = " +
                                        newTask.getId() + " with start time: " + newTask.getStartTime() +
                                        " with end time: " + newTask.getEndTime() + " and the task with id = " +
                                        taskPriority.getId() + " with start time: " + taskPriority.getStartTime() +
                                        " and " + " with end time: " + taskPriority.getEndTime());

                    } else {
                        System.out.println(
                                "TimeIntersections: the task with a name \"" + newTask.getName() + "\" with id = " +
                                        newTask.getId() + " with start time: " + newTask.getStartTime() +
                                        " with end time: " + newTask.getEndTime() + " and the task with id = " +
                                        taskPriority.getId() + " with start time: " + taskPriority.getStartTime() +
                                        " and " + " with end time: " + taskPriority.getEndTime());
                    }

                }
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(sortedTasks);
    }

    private void setEpicStatus(int epicId) {
        EpicTask epicTask = epicTaskById.get(epicId);
        if (epicTask.getSubtaskIds().isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else {
            int counterNew = 0;
            int counterDone = 0;

            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                TaskStatus status = subtaskById.get(subtaskId).getTaskStatus();
                if (status == TaskStatus.NEW) {
                    counterNew++;
                } else if (status == TaskStatus.DONE) {
                    counterDone++;
                } else {
                    epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                    return;
                }
            }
            if (counterNew == epicTask.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.NEW);
            } else if (counterDone == epicTask.getSubtaskIds().size()) {
                epicTask.setTaskStatus(TaskStatus.DONE);
            } else {
                epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    private void setEpicStartAndEndTime(int epicId) {
        EpicTask epicTask = epicTaskById.get(epicId);
        if (epicTask.getSubtaskIds().isEmpty()) {
            epicTask.setStartTime(Instant.MIN);
            epicTask.setDuration(0);
        } else {
            Instant minStartTime = Instant.MAX;
            Instant maxEndTime = Instant.MIN;

            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                Instant subtaskStartTime = subtaskById.get(subtaskId).getStartTime();
                Instant subtaskEndTime = subtaskById.get(subtaskId).getEndTime();
                if (subtaskStartTime.isBefore(minStartTime)) {
                    minStartTime = subtaskStartTime;
                }
                if (subtaskEndTime.isAfter(maxEndTime)) {
                    maxEndTime = subtaskEndTime;
                }
            }
            epicTask.setStartTime(minStartTime);
            epicTask.setEndTime(maxEndTime);
        }
    }

    static class TaskIdGenerator {
        private int nextFreedId;

        public void setNextFreedId(int nextFreedId) {
            this.nextFreedId = nextFreedId;
        }

        public int getNextFreedI() {
            return nextFreedId++;
        }
    }
}
