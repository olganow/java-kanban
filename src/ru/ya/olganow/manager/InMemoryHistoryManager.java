package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Объявите класс InMemoryHistoryManager и перенесите в него часть кода для работы с историей из класса InMemoryTaskManager.
    // Новый класс InMemoryHistoryManager должен реализовывать интерфейс HistoryManager.

    private final CustomLinkedList historyList = new CustomLinkedList();

    //для добавления нового просмотра задачи
    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    //для удаления просмотра из истории
    @Override
    public void remove(int id) {
        if (historyList.historyMap.containsKey(id)) {
            historyList.removeNode(historyList.historyMap.remove(id));
        }
    }

    //история последних просмотров
    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    private static class CustomLinkedList {
        //Указатель на первый элемент списка. Он же first
        private Node head;
        //Указатель на последний элемент списка. Он же last
        private Node tail;
        //CustomLinkedList собирает все задачи из MyLinkedList в обычный ArrayList
        private final Map<Integer, Node> historyMap = new HashMap<>();

        //добавление задачи в конец списка
        private void linkLast(Task task) {
            if (historyMap.containsKey(task.getId())) {
                removeNode(historyMap.get(task.getId()));
            }
            final Node newNode = new Node(tail, task, null);
            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            historyMap.put(task.getId(), newNode);
        }

        //Надо ещё на всякий случай проверить, что голова и хвост совпадают с node
        private void removeNode(Node node) {
            final Node next = node.getNext();
            final Node prev = node.getPrev();
            if (head == tail) {
                if (head == node) {
                    head = null;
                    tail = null;
                    return;
                }
                return;
            }
            if (node.getPrev() != null) {
                if (next == null) {
                    prev.setNext(null);
                    tail = prev;
                } else {
                    next.setPrev(prev);
                    prev.setNext(next);
                }
            } else if (node.getNext() != null) {
                next.setPrev(null);
                head = next;
            }
        }


        //Собирает все задачи из MyLinkedList в обычный ArrayList
        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = head;
            while (current != null) {
                tasks.add(current.getData());
                current = current.getNext();
            }
            return tasks;
        }

    }


    private static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }

        public void setData(Task data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

}

