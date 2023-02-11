package ru.ya.olganow.manager;

import ru.ya.olganow.task.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Объявите класс InMemoryHistoryManager и перенесите в него часть кода для работы с историей из класса InMemoryTaskManager.
    // Новый класс InMemoryHistoryManager должен реализовывать интерфейс HistoryManager.

    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    //для добавления нового просмотра задачи
    @Override
    public void add(Task task) {
            historyList.linkLast(task);
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

    private static class CustomLinkedList<E extends Task> {
        //Указатель на первый элемент списка. Он же first
        private Node head;
        //Указатель на последний элемент списка. Он же last
        private Node tail;
        private int size = 0;
        //CustomLinkedList собирает все задачи из MyLinkedList в обычный ArrayList
        private final Map<Integer, Node> historyMap = new HashMap<>();

        //добавление задачи в конец списка
        private void linkLast(Task task) {
          if (!historyMap.containsKey(task.getId())) {
              if (size == 0) {
                  final Node oldHead = head;
                  final Node newNode = new Node(null, task, oldHead);
                  historyMap.put(task.getId(), newNode);
                  head = newNode;
                  //add first
                  if (oldHead == null) {
                      tail = newNode;
                  } else {
                      oldHead.prev = newNode;
                  }
                  size++;
              } else {
                  final Node oldTail = tail;
                  Node newNode = new Node(oldTail, task, null);
                  historyMap.put(task.getId(), newNode);
                  tail = newNode;
                  if (oldTail == null) {
                      head = newNode;
                  } else {
                      oldTail.next = newNode;
                  }
                  size++;
              }
          }
          else {
              removeNode(historyMap.get(task.getId()));
              historyMap.remove(task.getId());
              linkLast(task);
              size--;
          }
        }

        private void removeNode(Node node) {
            if (head == tail) {
                head = null;
                tail = null;
            }

            if (node.getPrev() != null) {
                if (node.getNext() == null) {
                    node.getPrev().setNext(null);
                    tail = node.getPrev();
                } else {
                    node.getNext().setPrev(node.getPrev());
                    node.getPrev().setNext(node.getNext());
                }
            } else if (node.getNext() != null) {
                node.getNext().setPrev(null);
                head = node.getNext();
            }
        }


        //Собирает все задачи из MyLinkedList в обычный ArrayList
        private List getTasks() {
            List tasks = new ArrayList<>();
            if (head != null) {
                Node newNode = head;
                while (newNode != null) {
                    tasks.add(newNode.getData());
                    newNode = newNode.getNext();
                }
                return tasks;
            } else
                return tasks;
        }

    }


    static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node( Node prev, Task data,Node next) {
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

