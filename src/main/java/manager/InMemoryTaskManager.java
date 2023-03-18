package main.java.manager;

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
            if (o2.getStartTime() != null && o1.getStartTime() != null) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            } else if (o2.getStartTime() == null && o1.getStartTime() == null) {
                return o1.getId() - o2.getId();
            } else if (o1.getStartTime() == null) {
                return 1;
            } else return -1;
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    //option 1: how to save object with generated id in hashmap
    public void addSingleTask(SingleTask singleTask) {
        if (singleTask != null) {
            // 1: generate new id and save it to the task
            singleTask.setId(taskIdGenerator.getNextFreedI());
            // 2: save task
            validateTaskTimeIntersections(singleTask);
            singleTaskById.put(singleTask.getId(), singleTask);
            sortedTasks.add(singleTask);
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        if (epicTask != null) {
            // 1: generate new id and save it to the task
            epicTask.setId(taskIdGenerator.getNextFreedI());
            // 2: save task
            epicTaskById.put(epicTask.getId(), epicTask);
            // 3: Epic Status updated
            setEpicStatus(epicTask.getId());
            setEpicStartAndEndTime(epicTask.getId());
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public void addNewSubTask(Subtask subtask) {
        if (subtask != null) {
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
        } else throw new ManagerSaveException("Такой задачи нет");
    }

    @Override
    public void deleteAllSingleTask() {
        for (int id : singleTaskById.keySet()) {
            sortedTasks.remove(singleTaskById.get(id));
            historyManager.remove(id);
        }
        singleTaskById.clear();
    }

    public void deleteAllEpicTask() {
        for (int id : epicTaskById.keySet()) {
            historyManager.remove(id);
        }
        epicTaskById.clear();

        for (int id : subtaskById.keySet()) {
            sortedTasks.remove(subtaskById.get(id));
            historyManager.remove(id);
        }
        subtaskById.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (int id : subtaskById.keySet()) {
            sortedTasks.remove(subtaskById.get(id));
            historyManager.remove(id);
        }
        subtaskById.clear();
        for (EpicTask epic : epicTaskById.values()) {
            epic.getSubtaskIds().clear();
            setEpicStatus(epic.getId());
            setEpicStartAndEndTime(epic.getId());
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
            Subtask subtask = subtaskById.remove(id);
            sortedTasks.remove(subtask);
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
            sortedTasks.remove(singleTask.getId());
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
            sortedTasks.remove(subtask.getId());
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
            return new ArrayList<>(this.singleTaskById.values());
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllEpicTasks() {
        List<Task> tasks = new ArrayList<>(this.epicTaskById.values());
        if (tasks.isEmpty()) {
            return new ArrayList<>(this.epicTaskById.values());
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllSubtasks() {
        List<Task> tasks = new ArrayList<>(this.subtaskById.values());
        if (tasks.isEmpty()) {
            return new ArrayList<>(this.subtaskById.values());
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
                    if (taskPriority.getStartTime() == null || taskPriority.getEndTime() == null) {
                        break;
                    }
                    if (newTask.getId() == taskPriority.getId()) {
                        continue;
                    }
                    if (!newTask.getEndTime().isAfter(taskPriority.getStartTime())
                            || !newTask.getStartTime().isBefore(taskPriority.getEndTime())) {
                        continue;
                    } else
                        throw new ManagerSaveException(
                                "TimeIntersections: the task with a name \"" + newTask.getName() + "\" with id = " +
                                        newTask.getId() + " with start time: " + newTask.getStartTime() +
                                        " with end time: " + newTask.getEndTime() + " and the task with id = " +
                                        taskPriority.getId() + " with start time: " + taskPriority.getStartTime() +
                                        " and " + " with end time: " + taskPriority.getEndTime());

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
            epicTask.setStartTime(null);
            epicTask.setEndTime(null);
            epicTask.setDuration(0);
        } else {
            Instant minStartTime = null;
            Instant maxEndTime = null;
            long duration = 0;
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                Instant subtaskStartTime = subtaskById.get(subtaskId).getStartTime();
                Instant subtaskEndTime = subtaskById.get(subtaskId).getEndTime();
                long subtaskDuration = subtaskById.get(subtaskId).getDuration();
                if (subtaskStartTime != null) {
                    if (minStartTime == null || subtaskStartTime.isBefore(minStartTime)) {
                        minStartTime = subtaskStartTime;
                    }
                }
                if (subtaskEndTime != null) {
                    if (maxEndTime == null || subtaskEndTime.isAfter(maxEndTime)) {
                        maxEndTime = subtaskEndTime;
                    }
                }
                duration = duration + subtaskDuration;
            }
            epicTask.setStartTime(minStartTime);
            epicTask.setEndTime(maxEndTime);
            epicTask.setDuration(duration);
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
