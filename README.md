# Техническое задание java-kanban

Как человек обычно делает покупки? Если ему нужен не один продукт, а несколько, то очень вероятно,  
что сначала он составит список, чтобы ничего не забыть. Сделать это можно где угодно: на листе бумаги,
в приложении для заметок или, например, в сообщении самому себе в мессенджере.
А теперь представьте, что это список не продуктов, а полноценных дел. И не каких-нибудь простых вроде 
«помыть посуду» или «позвонить бабушке», а сложных — например, «организовать большой семейный праздник» 
или «купить квартиру». Каждая из таких задач может разбиваться на несколько этапов со своими нюансами и
сроками. А если над их выполнением будет работать не один человек, а целая команда, 
то организация процесса станет ещё сложнее.

## Технологии

Java, RESTful API, JUnit.

## Трекер задач

Как системы контроля версий помогают команде работать с общим кодом, так и трекеры задач позволяют эффективно 
организовать совместную работу над задачами. Вам предстоит написать бэкенд для такого трекера. 
В итоге должна получиться программа, отвечающая за формирование модели данных для этой страницы:
* To do
* In progress
* Done

## Типы задач

Простейшим кирпичиком такой системы является задача (англ. task). У задачи есть следующие свойства:

1. Название, кратко описывающее суть задачи (например, «Переезд»).
2. Описание, в котором раскрываются детали.
3. Уникальный идентификационный номер задачи, по которому её можно будет найти.
4. Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
   4.1. NEW — задача только создана, но к её выполнению ещё не приступили.
   4.2. IN_PROGRESS — над задачей ведётся работа.
   4.3. DONE — задача выполнена.

Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). 
Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic).
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. 
Для них должны выполняться следующие условия:
1. Для каждой подзадачи известно, в рамках какого эпика она выполняется.
2. Каждый эпик знает, какие подзадачи в него входят.
3. Завершение всех подзадач эпика считается завершением эпика.

## Идентификатор задачи

У каждого типа задач есть идентификатор. Это целое число, уникальное для всех типов задач. 
По нему мы находим, обновляем, удаляем задачи. 
При создании задачи менеджер присваивает ей новый идентификатор.

## Менеджер

Кроме классов для описания задач, вам нужно реализовать класс для объекта-менеджера. Он будет запускаться на старте
программы и управлять всеми задачами. В нём должны быть реализованы следующие функции:

1. Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
   2.1. Получение списка всех задач.
   2.2. Удаление всех задач.
   2.3. Получение по идентификатору.
   2.4. Создание. Сам объект должен передаваться в качестве параметра.
   2.5. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
   2.6. Удаление по идентификатору.
3. Дополнительные методы:
    3.1. Получение списка всех подзадач определённого эпика.
4. Управление статусами осуществляется по следующему правилу:
    4.1. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией 
         о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
   4.2. Для эпиков:  
        если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть "NEW".
        если все подзадачи имеют статус "DONE", то и эпик считается завершённым — со статусом "DONE".
        во всех остальных случаях статус должен быть "IN_PROGRESS".

### Хранения задач: в файле и HTTP-серверу

На первоначальном этапе разработки задачи хранились в мапах, потом в файлах, а в итоговом варианте хранение было 
перенесено на сервер. Для этого был написан HTTP-клиент, с его помощью было перемещено хранение состояния менеджера 
из файлов на отдельный сервер.

API работает так, чтобы все запросы по пути /tasks/<ресурсы> приходили в интерфейс TaskManager. 
Путь для обычных задач — /tasks/task, для подзадач — /tasks/subtask, для эпиков — /tasks/epic.
Получить все задачи сразу можно по пути /tasks/, а получить историю задач по пути /tasks/history. 

### Тестирование

В данный проект добавлен JUnit в проект и написано 200 юнит тестов.

Написан отдельный тест для каждого публичного метода: стандартный кейс его работы и граничные случаи.

Для расчёта статуса Epic. Граничные условия:
a. Пустой список подзадач.
b. Все подзадачи со статусом NEW.
c. Все подзадачи со статусом DONE.
d. Подзадачи со статусами NEW и DONE.
e. Подзадачи со статусом IN_PROGRESS.
Для двух менеджеров задач InMemoryTasksManager и FileBackedTasksManager.
Для подзадач проверено наличие эпика, а для эпика — расчёт статуса.

Для каждого метода проверена работа:  
a. Со стандартным поведением.
b. С пустым списком задач.
c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).

Для HistoryManager — тесты для всех методов интерфейса.
Граничные условия:  
a. Пустая история задач.
b. Дублирование.  
с. Удаление из истории: начало, середина, конец. 

Дополнительно для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. 
Граничные условия:  
a. Пустой список задач.
b. Эпик без подзадач.
c. Пустой список истории.
