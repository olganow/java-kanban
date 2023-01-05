package ru.ya.olganow;

import ru.ya.olganow.manager.TaskManager;
import ru.ya.olganow.task.Subtask;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
     //   System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int command = scanner.nextInt();

            switch (command){
                case 1:
                    //Получить список всех задач

                    break;

                case 2:
                    //Удалить все задачи
                    break;

                case 3:
                    //Получить по идентификатору

                    break;

                case 4:
                    //Создание. Сам объект должен передаваться в качестве параметра

                    break;

                case 5:
                    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
                    break;

                case 6:
                    //Удаление по идентификатору

                    break;

                case 7:
                    //Получение списка всех подзадач определённого эпика.

                    break;

                case 8:
                    System.out.println("8 - Выйти из программ");
                    return;

                default:
                    System.out.println("Простите, такой команды нет");
            }
        }
    }

    public static void printMenu() {
        System.out.println();
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Удалить все задачи");
        System.out.println("3 - Получить по идентификатору");
        System.out.println("4 - Создание. Сам объект должен передаваться в качестве параметра");
        System.out.println("5 - Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.");
        System.out.println("6 - Удаление по идентификатору.");
        System.out.println("7 - Получение списка всех подзадач определённого эпика.");
        System.out.println("8 - Выйти из программы");
    }
}
