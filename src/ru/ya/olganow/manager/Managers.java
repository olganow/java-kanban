package ru.ya.olganow.manager;

import java.io.File;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("src/resourсes/history.csv"));
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


