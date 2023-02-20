package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskStatus;
import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.io.*;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File historyFile;
    public static final String TITLE_LINE = "id,type,name,status,description,epic\n";

    public FileBackedTasksManager(File historyFile) {
        this.historyFile = historyFile;
    }

    // Сохранение в файл
    public void save() {
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
            throw new ManagerSaveException();
        }
    }

    //Сохранения задачи в строк
    private String toString(Task task) {
        String result = "";
        if (task.getTaskType() == TaskType.SINGLE || task.getTaskType() == TaskType.EPIC) {
            result = String.format("%d,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription()
            );
        }
        if (task.getTaskType() == TaskType.SUBTASK) {
            result = String.format("%d,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription(),
                    subtaskById.get(task.getId()).getEpicId()
            );
        }
        return result;
    }

    //Сохранение менеджера истории с CSV
    private static String historyToStringHistory(HistoryManager manager) {
        List<Task> historyTasks = manager.getHistory();
        StringBuilder historyIDs = new StringBuilder();
        for (int i = 0; i < historyTasks.size(); i++) {
            historyIDs.append(String.format("%d,", historyTasks.get(i).getId()));
        }
        return historyIDs.toString();
    }

    //Создания задачи из строки
    private Task fromString(String value) {
        Task task = null;
        String[] taskOptions = value.split(",");
        if (taskOptions[1].equals("SINGLE")) {
            task = new SingleTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]));

        } else if (taskOptions[1].equals("SUBTASK")) {
            task = new Subtask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]), Integer.parseInt(taskOptions[5]));

        } else if (taskOptions[1].equals("EPIC")) {
            ArrayList<Integer> subtaskIds = new ArrayList<>();
            task = new EpicTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[3], subtaskIds);
        }
        return task;
    }

    //Сохранения и восстановления менеджера истории из CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> historyFromString = new ArrayList<>();
        final String[] historyIds = value.split(",");
        for (String id : historyIds) {
            historyFromString.add(Integer.valueOf(id));
        }
        return historyFromString;
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
    public void addSingleTask(SingleTask singleTask) {
        super.addSingleTask(singleTask);
        save();
    }

    public void loadFromFile(String path) {
        try {
            String historyFileContent = Files.readString(Paths.get(path));

            int indexOfBreak = historyFileContent.indexOf("\n\n");
            String contentWithTasksWithTitle = historyFileContent.substring(1, indexOfBreak + 1);
            String contentWithHistory = historyFileContent.substring(indexOfBreak + 2);
            String contentWithTasks = contentWithTasksWithTitle.substring(TITLE_LINE.length() - 1);
            System.out.println(contentWithTasks);

            //Восстановление списка задач из файла
            final String[] content = contentWithTasks.split("\n");
            for (String taskFromString : content) {
                Task task = fromString(taskFromString);
                String taskType = task.getTaskType().toString();
                switch (taskType) {
                    case ("SINGLE"):
                        singleTaskById.put(task.getId(), (SingleTask) task);
                        break;
                    case ("SUBTASK"):
                        subtaskById.put(task.getId(), (Subtask) task);
                        break;
                    case ("EPIC"):
                        epicTaskById.put(task.getId(), (EpicTask) task);
                        break;
                }
                for (Subtask subtask : subtaskById.values()) {
                    epicTaskById.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
                }
            }

            //Восстановление истории из файла
            Task task = null;
            for (int id : historyFromString(contentWithHistory)) {
                if (singleTaskById.containsKey(id)) {
                    task = singleTaskById.get(id);
                    historyManager.add(task);
                } else if (epicTaskById.containsKey(id)) {
                    task = epicTaskById.get(id);
                    historyManager.add(task);
                } else if (subtaskById.containsKey(id)) {
                    task = subtaskById.get(id);
                    historyManager.add(task);
                }
            }
            if (task != null)
                historyManager.add(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("src/resourсes/history.csv"));
        fileBackedTasksManager.loadFromFile("src/resourсes/history.csv");

        //  System.out.println("Получить список всех одиночных задач\n" + fileBackedTasksManager.getAllSingleTasks());
        System.out.println("Получить список всех эпиков\n" + fileBackedTasksManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + fileBackedTasksManager.getAllSubtasks());

        //Get search history
        System.out.println("Получить историю поиска:\n" + fileBackedTasksManager.getHistory());

    }
}