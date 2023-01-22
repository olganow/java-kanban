package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;

import java.util.List;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static List<Task> getDefaultHistory() {

        return new InMemoryHistoryManager().getHistory();
    }
}


