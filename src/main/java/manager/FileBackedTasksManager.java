
package main.java.manager;

import main.java.description.TaskStatus;
import main.java.description.TaskType;
import main.java.task.SingleTask;
import main.java.task.Task;
import main.java.task.EpicTask;
import main.java.task.Subtask;

import java.io.*;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File historyFile;
    private static final String TITLE_LINE = "id,type,name,status,description,startTime,duration,endTime,epic\n";

    public FileBackedTasksManager(File historyFile) {
        this.historyFile = historyFile;
    }

    @Override
    public void addSingleTask(SingleTask singleTask) {
        super.addSingleTask(singleTask);
        save();
    }

    @Override
    public void addNewSubTask(Subtask subtask) {
        super.addNewSubTask(subtask);
        save();
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteAllSingleTask() {
        super.deleteAllSingleTask();
        save();
    }

    @Override
    public void deleteAllEpicTask() {
        super.deleteAllEpicTask();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void updateSingleTask(SingleTask singleTask) {
        super.updateSingleTask(singleTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    protected void save() {
        try (Writer fileWriter = new FileWriter(historyFile)) {
            fileWriter.write(TITLE_LINE);

            for (SingleTask singleTask : singleTaskById.values()) {
                fileWriter.write(toString(singleTask) + "\n");
            }

            for (EpicTask epic : epicTaskById.values()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtaskById.values()) {
                fileWriter.write(toString(subtask) + "\n");
            }
            if (historyManager.getHistory() != null)
                fileWriter.write("\n");
            fileWriter.write(historyToStringHistory(historyManager));

        } catch (IOException ex) {
            throw new ManagerSaveException("Такого файла нет");
        }
    }

    private String toString(Task task) {
        String result = "";
        if (task.getTaskType() == TaskType.SINGLE || task.getTaskType() == TaskType.EPIC) {
            result = String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription(),
                    task.getStartTime(),
                    task.getDuration(),
                    task.getEndTime()
            );
        }
        if (task.getTaskType() == TaskType.SUBTASK) {
            result = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription(),
                    task.getStartTime(),
                    task.getDuration(),
                    task.getEndTime(),
                    ((Subtask) task).getEpicId()
            );
        }
        return result;
    }

    //Сохранение менеджера истории в CSV
    private static String historyToStringHistory(HistoryManager manager) {
        List<Task> historyTasks = manager.getHistory();
        StringBuilder historyIDs = new StringBuilder();
        for (int i = 0; i < historyTasks.size(); i++) {
            historyIDs.append(historyTasks.get(i).getId());
            if (i != historyTasks.size() - 1) {
                historyIDs.append(",");
            }
        }
        return historyIDs.toString();
    }

    //Создания задачи из строки
    private static Task fromString(String value) {
        Task task = null;
        String[] taskOptions = value.split(",");
        TaskType taskType = TaskType.valueOf(taskOptions[1]);
        Instant startTime = taskOptions[5].equals("null") ? null : Instant.parse(taskOptions[5]);
        long duration = taskOptions[6].equals("null") ? 0 : Long.parseLong(taskOptions[6]);
        switch (taskType) {
            case SINGLE:
                task = new SingleTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]),
                        startTime, duration);
                break;
            case SUBTASK:
                task = new Subtask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]),
                        startTime, duration, Integer.parseInt(taskOptions[8]));
                break;
            case EPIC:
                Instant endTime = taskOptions[7].equals("null") ? null : Instant.parse(taskOptions[7]);
                task = new EpicTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]),
                        startTime, duration);
                ((EpicTask) task).setEndTime(endTime);
                break;
        }
        return task;
    }

    //Сохранения и восстановления менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        List<Integer> historyFromString = new ArrayList<>();
        if (!value.isEmpty()) {
            final String[] historyIds = value.split(",");
            for (String id : historyIds) {
                historyFromString.add(Integer.valueOf(id));
            }
        }
        return historyFromString;
    }

    static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager newTaskManager = new FileBackedTasksManager(new File(path));
        try {
            String historyFileContent = Files.readString(Paths.get(path));

            if (historyFileContent.isEmpty()) {
                return newTaskManager;
            }

            int indexOfBreak = historyFileContent.indexOf("\n\n");
            String contentWithTasksWithTitle = historyFileContent.substring(1, indexOfBreak + 1);
            String contentWithHistory = historyFileContent.substring(indexOfBreak + 2);
            String contentWithTasks = contentWithTasksWithTitle.substring(TITLE_LINE.length() - 1);

            //Восстановление списка задач из файла
            final String[] content = contentWithTasks.split("\n");
            for (String taskFromString : content) {
                if (taskFromString.isEmpty()) {
                    break;
                }
                Task task = fromString(taskFromString);
                TaskType taskType = task.getTaskType();
                int taskID = task.getId();
                if (newTaskManager.taskIdGenerator.getNextFreedI() < taskID) {
                    newTaskManager.taskIdGenerator.setNextFreedId(taskID);
                }
                switch (taskType) {
                    case SINGLE:
                        newTaskManager.singleTaskById.put(taskID, (SingleTask) task);
                        newTaskManager.sortedTasks.add(task);
                        break;
                    case SUBTASK:
                        newTaskManager.subtaskById.put(taskID, (Subtask) task);
                        int epicID = ((Subtask) task).getEpicId();
                        newTaskManager.epicTaskById.get(epicID).getSubtaskIds().add(taskID);
                        newTaskManager.sortedTasks.add(task);
                        break;
                    case EPIC:
                        newTaskManager.epicTaskById.put(taskID, (EpicTask) task);
                        break;
                }
            }

            //Восстановление истории из файла
            Task task;
            for (int id : historyFromString(contentWithHistory)) {
                if (newTaskManager.singleTaskById.containsKey(id)) {
                    task = newTaskManager.singleTaskById.get(id);
                    newTaskManager.historyManager.add(task);
                } else if (newTaskManager.epicTaskById.containsKey(id)) {
                    task = newTaskManager.epicTaskById.get(id);
                    newTaskManager.historyManager.add(task);
                } else if (newTaskManager.subtaskById.containsKey(id)) {
                    task = newTaskManager.subtaskById.get(id);
                    newTaskManager.historyManager.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newTaskManager;
    }


    public static void main(String[] args) {
        File file = new File("src/main/resourсes/history.csv");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file.getPath());

        // Two single task created
        SingleTask singleTask = new SingleTask("Single safe Task", "Desc SST", TaskStatus.NEW);
        fileBackedTasksManager.addSingleTask(singleTask);
        SingleTask singleTask2 = new SingleTask("Another safe Task", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(1704056400000L), 707568400L);
        fileBackedTasksManager.addSingleTask(singleTask2);

//        EpicTask epicTask2 = new EpicTask("New epic", "Desc FE");
//        fileBackedTasksManager.addEpicTask(epicTask2);

        System.out.println("\nИстория из файла загружена");
        fileBackedTasksManager.loadFromFile(file.getPath());

        System.out.println("Получить список всех одиночных задач\n" + fileBackedTasksManager.getAllSingleTasks());
        //  System.out.println("Получить список всех эпиков\n" + fileBackedTasksManager.getAllEpicTasks());
        //  System.out.println("Получить список всех подзадач\n" + fileBackedTasksManager.getAllSubtasks());
        System.out.println("Получить отсортированные по времени начала задачи и сабтаски:\n" + fileBackedTasksManager.getPrioritizedTasks());

    }
}
