package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;

import java.util.List;
//У него будет два метода. Первый add(Task task) должен помечать задачи как просмотренные,
// а второй getHistory() — возвращать их

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

}
