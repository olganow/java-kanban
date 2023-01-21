package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Объявите класс InMemoryHistoryManager и перенесите в него часть кода для работы с историей из класса InMemoryTaskManager.
    // Новый класс InMemoryHistoryManager должен реализовывать интерфейс HistoryManager.
    public static final Integer SIZE_MAX_HISTORY = 10;
    private static final List<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() < SIZE_MAX_HISTORY) {
            historyList.add(task);
        } else {
            historyList.remove(0);
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
