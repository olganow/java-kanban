package ru.ya.olganow.manager;

import ru.ya.olganow.description.TaskType;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;
import ru.ya.olganow.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File historyFile;
    public static final String TITLE_LINE = "id,type,name,status,description,epic\n";

    public FileBackedTasksManager(File historyFile) {
        this.historyFile = historyFile;
    }

    //    Создайте enum с типами задач.
    //    Напишите метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
    //    Напишите метод создания задачи из строки Task fromString(String value).
    //    Напишите статические методы static String historyToString(HistoryManager manager) и static List<Integer> historyFromString(String value) для сохранения и восстановления менеджера истории из CSV.

    // Сохранение в файл
    public void save(Task task) throws IOException {
        Writer fileWriter = new FileWriter(historyFile);
        fileWriter.write(TITLE_LINE);
        fileWriter.write(toString(task));
        fileWriter.close();
}

    private String toString(Task task){
        String result = "";
        if (task.getTaskType() == TaskType.SINGLE) {
            result = String.format("%d,%s,%s,%s",
                    task.getId(),
                    task.getTaskType(),
                    task.getName(),
                    task.getTaskStatus()
            )+"\n";
        }
        return result;
    }

    static String historyToString(HistoryManager manager){

        String historyToString = manager.toString();
    return historyToString;
    }

//    static List<Integer> historyFromString(String value){
//
//    }

    @Override
    public void saveNewSubTask(Subtask subtask) {
        super.saveNewSubTask(subtask);
        try {
            save(subtask);
        } catch (IOException e){
            System.out.println("ошибочка");
        }
    }
    @Override
    public void saveSingleTask(SingleTask singleTask) {
        super.saveSingleTask(singleTask);
        try {
            save(singleTask);
        } catch (IOException e){
            System.out.println("ошибочка");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
        return manager;
    }

}
