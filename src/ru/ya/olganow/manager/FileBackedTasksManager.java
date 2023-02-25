
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
    public Task getTaskById(int id)  {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    // Сохранение в файл
    private void save() {
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

    //Сохранения задачи в строки
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
                    ((Subtask)task).getEpicId()
            );
        }
        return result;
    }

    //Сохранение менеджера истории с CSV
    private static String historyToStringHistory(HistoryManager manager) {
        List<Task> historyTasks = manager.getHistory();
        StringBuilder historyIDs = new StringBuilder();
        for (int i = 0; i < historyTasks.size(); i++) {
            //последнего элемента истории из файла - вывод без запятой
            if (i == historyTasks.size() - 1)
                historyIDs.append(String.format("%d", historyTasks.get(i).getId()));
            else
                historyIDs.append(String.format("%d,", historyTasks.get(i).getId()));
        }
        return historyIDs.toString();
    }

    //Создания задачи из строки
    private Task fromString(String value) {
        Task task = null;
        String[] taskOptions = value.split(",");
        TaskType taskType = TaskType.valueOf(taskOptions[1]);
        switch (taskType) {
            case SINGLE:
                task = new SingleTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]));
                break;
            case SUBTASK:
                task = new Subtask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[4], TaskStatus.valueOf(taskOptions[3]), Integer.parseInt(taskOptions[5]));
                break;
            case EPIC:
                task = new EpicTask(Integer.parseInt(taskOptions[0]), taskOptions[2], taskOptions[3]);
                break;
        }
        return task;
    }

    //Сохранения и восстановления менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        List<Integer> historyFromString = new ArrayList<>();
        final String[] historyIds = value.split(",");
        for (String id : historyIds) {
            historyFromString.add(Integer.valueOf(id));
        }
        return historyFromString;
    }

    public void loadFromFile(String path) {
        try {
            String historyFileContent = Files.readString(Paths.get(path));

            int indexOfBreak = historyFileContent.indexOf("\n\n");
            String contentWithTasksWithTitle = historyFileContent.substring(1, indexOfBreak + 1);
            String contentWithHistory = historyFileContent.substring(indexOfBreak + 2);
            String contentWithTasks = contentWithTasksWithTitle.substring(TITLE_LINE.length() - 1);

            //Восстановление списка задач из файла
            final String[] content = contentWithTasks.split("\n");
            for (String taskFromString : content) {
                Task task = fromString(taskFromString);
                TaskType taskType = task.getTaskType();
                switch (taskType) {
                    case SINGLE:
                        singleTaskById.put(task.getId(), (SingleTask) task);
                        break;
                    case SUBTASK:
                        subtaskById.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        epicTaskById.put(task.getId(), (EpicTask) task);
                        break;
                }
            }

            //Добавления сабтасок к эпикам
            for (Subtask subtask : subtaskById.values()) {
                epicTaskById.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
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

        // Two single task created
        SingleTask singleTask = new SingleTask("Single safe Task", "Desc SST", TaskStatus.NEW);
        fileBackedTasksManager.addSingleTask(singleTask);
        SingleTask singleTask2 = new SingleTask("Another safe Task", "Desc AST", TaskStatus.NEW);
        fileBackedTasksManager.addSingleTask(singleTask2);


        //Two Epic created
        EpicTask epicTask1 = new EpicTask("First epic", "Desc FE");
        fileBackedTasksManager.addEpicTask(epicTask1);
        Subtask subtask1 = new Subtask("First subtask", "Desc FSB", TaskStatus.NEW, 2);
        fileBackedTasksManager.addNewSubTask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "Desc SSB", TaskStatus.NEW, 2);
        fileBackedTasksManager.addNewSubTask(subtask2);

        EpicTask epicTask2 = new EpicTask("Second epic", "Desc FE");
        fileBackedTasksManager.addEpicTask(epicTask2);
        Subtask subtask3 = new Subtask("Third subtask", "Desc FSB", TaskStatus.DONE, 5);
        fileBackedTasksManager.addNewSubTask(subtask3);

        //Get all tasks by Id
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(0));
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(1));
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(2));
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(3));
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(4));
        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(5));


        System.out.println("Получить список всех одиночных задач\n" + fileBackedTasksManager.getAllSingleTasks());
        System.out.println("Получить список всех эпиков\n" + fileBackedTasksManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + fileBackedTasksManager.getAllSubtasks());

        //Get search history
        System.out.println("Получить историю поиска:\n" + fileBackedTasksManager.getHistory());

        //Delete by ID
        System.out.println("Задача с указанным id удалена");
        fileBackedTasksManager.deleteById(5);

        Subtask subtask4 = new Subtask("4 subtask", "Desc 4SB", TaskStatus.IN_PROGRESS, 2);
        fileBackedTasksManager.addNewSubTask(subtask4);


        System.out.println("\nЗагрузить историю из файла");
        fileBackedTasksManager.loadFromFile("src/resourсes/history.csv");

        System.out.println("Получить список всех одиночных задач\n" + fileBackedTasksManager.getAllSingleTasks());
        System.out.println("Получить список всех эпиков\n" + fileBackedTasksManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + fileBackedTasksManager.getAllSubtasks());
        //Get search history
        System.out.println("Получить историю поиска:\n" + fileBackedTasksManager.getHistory());

        Subtask subtask5 = new Subtask("5 subtask", "Desc 5SB", TaskStatus.IN_PROGRESS, 2);
        fileBackedTasksManager.addNewSubTask(subtask5);
        SingleTask singleTask3 = new SingleTask("Another safe Task", "3Desc AST", TaskStatus.NEW);
        fileBackedTasksManager.addSingleTask(singleTask3);

        System.out.println("Получить по ID\n" + fileBackedTasksManager.getTaskById(8));


        System.out.println("Получить список всех одиночных задач\n" + fileBackedTasksManager.getAllSingleTasks());
        System.out.println("Получить список всех эпиков\n" + fileBackedTasksManager.getAllEpicTasks());
        System.out.println("Получить список всех подзадач\n" + fileBackedTasksManager.getAllSubtasks());
        //Get search history
        System.out.println("Получить историю поиска:\n" + fileBackedTasksManager.getHistory());

    }
}
