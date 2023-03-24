package main.java.manager;

import main.java.http.HttpTaskManager;
import main.java.http.KVServer;

import java.io.File;
import java.io.IOException;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("src/main/resourсes/history.csv"));
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefaultHttpTaskManager(String url) throws IOException {
        return new HttpTaskManager(url);
    }

    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }
}


