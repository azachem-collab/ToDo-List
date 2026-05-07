package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

class Task {
    private int id;
    private String title;
    private boolean completed;
    private static int counter = 0;

    //Пустой конструктор для Jackson
    public Task() {}

    //Метод при создании задачи
    public Task(String taskTitle) {
        this.title = taskTitle;
        this.id = ++counter;
        this.completed = false;
    }

    //Сеттеры
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public static void setCounter(int counter) {
        Task.counter = counter;
    }

    //Геттеры
    public String getTitle() {
        return this.title;
    }
    public int getId() {
        return this.id;
    }
    public boolean isCompleted() {
        return this.completed;
    }

}

//Основной класс программы
public class ToDoList {

    //Основной метод программы
    public static void main(String[] args) {

        //Объявляем нашу коллекцию объектов (с полями задач внутри)
        ArrayList<Task> tasksList = new ArrayList<>();

        //Чтение из файла JSON
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("tasks.json");

        if (file.exists()) {
            try {
                tasksList = mapper.readValue(
                        file,
                        new TypeReference<ArrayList<Task>>() {}
                );
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + e.getMessage());
            }
        }

        int maxId = 0;
        for (Task task : tasksList) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        Task.setCounter(maxId);  //Делаем переменную counter равную максимальному id

        int cmdChoose = 0;       //Переменная выбора действия

        //Объявление сканера
        Scanner input = new Scanner(System.in);

        while (cmdChoose != 5) {
            System.out.println("1. Добавить задачу\n2. Показать все задачи\n3. Изменить статус задачи\n4. Удалить задачу\n5. Выйти");
            try {
                String result = input.nextLine();
                cmdChoose = Integer.parseInt(result);
                switch (cmdChoose) {
                    case 1:
                        //Добавление задачи

                        System.out.println("Введите текст задачи\n");
                        String taskTitle = input.nextLine();

                        if (taskTitle.trim().isEmpty()) {
                            System.out.println("Название задачи не может быть пустым.");
                        } else {
                            Task task = new Task(taskTitle);
                            tasksList.add(task);
                            saveTasks(tasksList, mapper);
                            System.out.println("Задача добавлена.");
                        }

                        break;
                    case 2:
                        if (tasksList.isEmpty()){
                            System.out.println("А задачек-то нет!");
                        } else {
                            for (Task t : tasksList) {
                                String status = t.isCompleted() ? "[выполнено]" : "[не выполнено]";
                                System.out.println(t.getId() + ". " + t.getTitle() + " Статус: " + status + "\n");
                            }
                        }
                        break;
                    case 3: {
                        boolean found = false;
                        System.out.println("Введите номер задачи, чтобы сменить её статус.");
                        String stringId1 = input.nextLine();
                        try {
                            int numberId = Integer.parseInt(stringId1);
                            for (Task t : tasksList) {
                                if (t.getId() == numberId) {
                                    found = true;
                                    t.setCompleted(!t.isCompleted());
                                    saveTasks(tasksList, mapper);
                                    System.out.println("Статус задачи " + numberId + " изменён.");
                                    break;
                                }
                            }
                            if (!found) {
                                System.out.println("Такого номера задачи нет.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод. Пожалуйста, введите числовой ID задачи.");
                        }
                        break;
                    }
                    case 4: {
                        boolean found = false;
                        System.out.println("Введите номер задачи, чтобы удалить её.");
                        String stringId2 = input.nextLine();

                        try {
                            int deleteId = Integer.parseInt(stringId2);
                            //Пробегаемся циклом, ищем задачу и удаляем её
                            for (int i = 0; i < tasksList.size(); i++) {
                                Task t = tasksList.get(i);
                                if (t.getId() == deleteId) {
                                    found = true;
                                    tasksList.remove(i);
                                    break;
                                }
                            }
                            if (!found) {
                                System.out.println("Такого номера задачи нет.");
                            } else {
                                //Переписываем все id у всех элементов, чтобы новая задача создалась с корректным номером
                                for (int i = 0; i < tasksList.size(); i++) {
                                Task t = tasksList.get(i);
                                t.setId(i + 1);
                            }
                                Task.setCounter(tasksList.size());
                                saveTasks(tasksList, mapper);
                                System.out.println("Задача удалена.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод. Пожалуйста, введите числовой ID задачи.");
                        }
                        break;
                    }
                    case 5:
                        System.out.println("Выход...");

                        break;
                    default:
                        System.out.println("Вводить нужно цифры от 1 до 5");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите числовой код команды.");
            }
        }
    }
    private static void saveTasks(ArrayList<Task> tasksList, ObjectMapper mapper){
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(
                    new File("tasks.json"),
                    tasksList);
        }catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}