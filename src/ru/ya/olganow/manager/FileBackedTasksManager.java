package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File historyFile;
    public static final String TITLE_LINE = "id,type,name,status,description,epic\n";

    public FileBackedTasksManager(File historyFile) {
        this.historyFile = historyFile;
    }

    // Сохранение в файл
    public void save() throws IOException {
        Writer fileWriter = new FileWriter(historyFile);
        fileWriter.write(TITLE_LINE);

        for (SingleTask singleTask: singleTaskById.values()) {
            fileWriter.write(toString(singleTask) + "\n");
        }

        for (EpicTask epic : epicTaskById.values()) {
            fileWriter.write(toString(epic) + "\n");
        }
        for (Subtask subtask : subtaskById.values()) {
            fileWriter.write(toString(subtask) + "\n");
        }
        if ( historyManager.getHistory() != null)
            fileWriter.write("\n");
            fileWriter.write(toStringHistory(historyManager));
        fileWriter.close();
}

    private String toString(Task task){
        String result = "";
        if (task.getTaskType() == TaskType.SINGLE || task.getTaskType() == TaskType.SUBTASK ) {
            result = String.format("%d,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription()
            );
        }
        if (task.getTaskType() == TaskType.EPIC) {
            result = String.format("%d,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus(),
                    task.getDescription(),
                    epicTaskById.get(task.getId()).getSubtaskIds()
            );
        }
        return result;
    }

    private static String toStringHistory(HistoryManager manager) {
        List<Task> historyTasks = manager.getHistory();
        StringBuilder historyIDs = new StringBuilder();
        for (int i = 0; i < historyTasks .size(); i++) {
            historyIDs.append(String.format("%d,", historyTasks.get(i).getId()));
        }

        return historyIDs.toString();
    }

    @Override
    public void addNewSubTask(Subtask subtask) {
        super.addNewSubTask(subtask);
        try {
            save();
        } catch (IOException e){
            System.out.println("ошибочка");
        }
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        try {
            save();
        } catch (IOException e){
            System.out.println("ошибочка");
        }
    }

    @Override
    public void addSingleTask(SingleTask singleTask) {
        super.addSingleTask(singleTask);
        try {
            save();
        } catch (IOException e){
            System.out.println("ошибочка");
        }
    }

//    public static FileBackedTasksManager loadFromFile(File file) {
//        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
//        return manager;
//    }

}
