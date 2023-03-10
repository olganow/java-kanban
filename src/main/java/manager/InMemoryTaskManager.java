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
    protected TreeSet<Task> sortedTasks;

    public InMemoryTaskManager() {
        this.taskIdGenerator = new TaskIdGenerator();
        this.singleTaskById = new HashMap<>();
        this.epicTaskById = new HashMap<>();
        this.subtaskById = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.sortedTasks = new TreeSet<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    //option 1: how to save object with generated id in hashmap
    public void addSingleTask(SingleTask singleTask) {
        // 1: generate new id and save it to the task
        singleTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        // 1: generate new id and save it to the task
        epicTask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        epicTaskById.put(epicTask.getId(), epicTask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
        setEpicStartAndEndTime(epicTask.getId());
    }

    @Override
    public void addNewSubTask(Subtask subtask) {
        // 1: generate new id and save it to the task
        subtask.setId(taskIdGenerator.getNextFreedI());
        // 2: save task
        EpicTask epicTask = epicTaskById.get(subtask.getEpicId());
        Integer id = subtask.getId();
        epicTask.getSubtaskIds().add(id);
        subtaskById.put(id, subtask);
        // 3: Epic Status updated
        setEpicStatus(epicTask.getId());
        setEpicStartAndEndTime(epicTask.getId());
    }

    @Override
    public void deleteAllSingleTask() {
        for (int id : singleTaskById.keySet()) {
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
            historyManager.remove(id);
        }
        subtaskById.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (int id : subtaskById.keySet()) {
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
            singleTaskById.remove(id);
            System.out.println("Задача с id=" + id + " удалена");
            historyManager.remove(id);

        } else if (epicTaskById.containsKey(id)) {
            EpicTask epicTask = epicTaskById.remove(id);
            for (Integer subtaskId : epicTask.getSubtaskIds()) {
                subtaskById.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
            System.out.println("Задача с id=" + id + ", и ее сабтаски удалены");

        } else if (subtaskById.containsKey(id)) {
            Subtask subtask = subtaskById.remove(id);
            int epicId = subtask.getEpicId();
            historyManager.remove(id);
            System.out.println("Подзадача с id=" + id + " удалена");

            //Epic Status and Epic List updated
            epicTaskById.get(epicId).getSubtaskIds().remove((Integer.valueOf(id)));
            setEpicStatus(epicId);
            setEpicStartAndEndTime(epicId);
        } else {
            System.out.println("Такого id нет ");
        }
    }

    @Override
    public void updateSingleTask(SingleTask singleTask) {
        singleTaskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTaskById.put(epicTask.getId(), epicTask);
        //Epic Status updated
        setEpicStatus(epicTask.getId());
        setEpicStartAndEndTime(epicTask.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskById.put(subtask.getId(), subtask);
        //Epic Status updated
        setEpicStatus(subtask.getEpicId());
        setEpicStartAndEndTime(subtask.getEpicId());
    }

    @Override
    public List<Task> getAllSingleTasks() {
        List<Task> tasks = new ArrayList<>(this.singleTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllEpicTasks() {
        List<Task> tasks = new ArrayList<>(this.epicTaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return tasks;
        }
    }

    @Override
    public List<Task> getAllSubtasks() {
        List<Task> tasks = new ArrayList<>(this.subtaskById.values());
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException();
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
            throw new IllegalArgumentException();
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
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> allSingleTaskAndSubtasksTasks = new ArrayList<>(sortedTasks);
        allSingleTaskAndSubtasksTasks.addAll(getAllSubtasks());
        allSingleTaskAndSubtasksTasks.addAll(getAllSingleTasks());
        // компаратор по цене от раннего к позднему
        StartDataComparator itemPriceComparator = new StartDataComparator();
        // применяем его
        allSingleTaskAndSubtasksTasks.sort(itemPriceComparator);
        return allSingleTaskAndSubtasksTasks;
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
                long subtaskDuration = 0;
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

    static class StartDataComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {

            if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return -1;

            } else if (task2.getStartTime().isBefore(task1.getStartTime())) {
                return 1;

            } else {
                return 0;
            }
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
