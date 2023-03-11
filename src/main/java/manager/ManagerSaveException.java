package main.java.manager;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super();
    }

    public ManagerSaveException(String s) {
        System.out.println(s);
    }
}
