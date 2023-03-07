package main.java.manager;

import java.io.File;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("src/main/resourсes/history.csv"));
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


