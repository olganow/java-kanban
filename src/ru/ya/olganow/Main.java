package ru.ya.olganow;

import ru.ya.olganow.manager.TaskManager;
import ru.ya.olganow.description.Status;
import ru.ya.olganow.task.EpicTask;
import ru.ya.olganow.task.SingleTask;
import ru.ya.olganow.task.Subtask;

public class Main {

    public static void main(String[] args) {
     //   System.out.println("Поехали!");
       //TaskManager taskManager = new TaskManager();


//        SingleTask singleTas11 = new SingleTask(1,"Pure task", Status.NEW);
//        EpicTask epicTask = new EpicTask(2,"Pure task");
//        Subtask subtask = new Subtask(3, "SubTask",Status.NEW, epicTask);

        TaskManager taskManager = new TaskManager();
        SingleTask.ToCreate singleTaskToCreate = new SingleTask.ToCreate( "Single safe Task");
        taskManager.saveNewTask(singleTaskToCreate);

        taskManager.saveNewTask( new SingleTask.ToCreate( "Another safe Task"));


        SingleTask singleTask = (SingleTask) taskManager.getTaskById(0);
        System.out.println(taskManager.getTaskById(0));

        // how to change status
        singleTask.setStatus(Status.IN_PROGRESS);
        taskManager.update(singleTask);
        System.out.println(taskManager.getTaskById(0));

//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            printMenu();
//            int command = scanner.nextInt();
//
//            switch (command){
//                case 1:
//                    //Получить список всех задач
//
//                    break;
//
//                case 2:
//                    //Удалить все задачи
//                    break;
//
//                case 3:
//                    //Получить по идентификатору
//
//                    break;
//
//                case 4:
//                    //Создание. Сам объект должен передаваться в качестве параметра
//
//                    break;
//
//                case 5:
//                    // Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
//                    break;
//
//                case 6:
//                    //Удаление по идентификатору
//
//                    break;
//
//                case 7:
//                    //Получение списка всех подзадач определённого эпика.
//
//                    break;
//
//                case 8:
//                    System.out.println("8 - Выйти из программ");
//                    return;
//
//                default:
//                    System.out.println("Простите, такой команды нет");
//            }
//        }
    }

//    public static void printMenu() {
//        System.out.println();
//        System.out.println("Что вы хотите сделать? ");
//        System.out.println("1 - Получить список всех задач");
//        System.out.println("2 - Удалить все задачи");
//        System.out.println("3 - Получить по идентификатору");
//        System.out.println("4 - Создание. Сам объект должен передаваться в качестве параметра");
//        System.out.println("5 - Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.");
//        System.out.println("6 - Удаление по идентификатору.");
//        System.out.println("7 - Получение списка всех подзадач определённого эпика.");
//        System.out.println("8 - Выйти из программы");
//    }
}
