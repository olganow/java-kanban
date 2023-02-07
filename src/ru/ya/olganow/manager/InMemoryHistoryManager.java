package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Объявите класс InMemoryHistoryManager и перенесите в него часть кода для работы с историей из класса InMemoryTaskManager.
    // Новый класс InMemoryHistoryManager должен реализовывать интерфейс HistoryManager.

    //  private static final LinkedList<Task> historyList = new LinkedList<>();
    private static final int SIZE_MAX_HISTORY = 10;
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    //для добавления нового просмотра задачи
    @Override
    public void add(Task task) {
        if (historyList.size < SIZE_MAX_HISTORY) {
            historyList.linkLast(task);
        } else {
            // historyList.removeFirst();
            historyList.linkLast(task);
        }
    }

    //для удаления просмотра из истории
    @Override
    public void remove(int id) {
        if (!historyList.historyMap.isEmpty()) {
            historyList.removeNode(historyList.historyMap.get(id));
            historyList.historyMap.remove(id);
        }
    }

    //история последних просмотров
    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    private class CustomLinkedList<E extends Task> {
        //Указатель на первый элемент списка. Он же first
        private Node<E> head;
        //Указатель на последний элемент списка. Он же last
        private Node<E> tail;
        private int size = 0;
        //CustomLinkedList собирает все задачи из MyLinkedList в обычный ArrayList
        private final Map<Integer, Node<E>> historyMap = new HashMap<>();

        //добавление задачи в конец списка
        private void linkLast(E value) {
            // Реализуйте метод
            final Node<E> oldTail = tail;
            final Node<E> newNode = new Node<>(oldTail, value, null);
            tail = newNode;
            if (oldTail == null)
                tail = newNode;
            else
                oldTail.prev = newNode;
            size++;

        }


        private void removeNode(Node<E> value) {

        }

        //Собирает все задачи из MyLinkedList в обычный ArrayList
        private List<E> getTasks() {
            if (head != null) {
                Node<E> node = head;
                List<E> tasks = new ArrayList<>();
                while (true) {
                    tasks.add(node.getData());
                    if (node.getNext() == null)
                        break;
                    node = node.getNext();
                }
                return tasks;
            } else
                return null;
        }

    }


    class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> next, E data, Node<E> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }


        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        private Node<E> getPrev() {
            return prev;
        }

        private void setNext(Node<E> next) {
            this.next = next;
        }

        private void setPrev(Node<E> prev) {
            this.prev = prev;
        }
    }
}