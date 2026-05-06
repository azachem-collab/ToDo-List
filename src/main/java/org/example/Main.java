package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Tasks {
    private int id;
    private String title;
    private boolean completed;
    private static int counter = 0;

    //Метод при создании задачи
    public Tasks(String taskTitle) {
        this.title = taskTitle;
        this.id = ++counter;
        this.completed = false;
    }

    //Метод для записи задач из файла (парс строк)
    public Tasks(int id, String taskTitle, boolean completed){
        this.id = id;
        this.title = taskTitle;
        this.completed = completed;
        ++counter;
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
        Tasks.counter = counter;
    }

    //Геттеры
    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }

    public boolean getCompleted() {
        return this.completed;
    }

}

//Основной класс программы
class ToDoList {
    //Основной метод программы
    public static void main(String[] args) {


        ArrayList<Tasks> tasksList = new ArrayList<>();

        try(BufferedReader tasksReader = new BufferedReader(new FileReader("tasks.csv"))){
            String line;
            while ((line = tasksReader.readLine()) != null){
                String[] parts = line.split(";");

                //Вытаскиваем ID, title и completed из файла
                int partsInt = Integer.parseInt(parts[0]);
                boolean partsBoolean = Boolean.parseBoolean(parts[2]);
                Tasks task = new Tasks(partsInt, parts[1], partsBoolean);
                tasksList.add(task);
            }
        }catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }


        int cmdChoose = 0;
        boolean flag = false;

        Scanner input = new Scanner(System.in);

        while (cmdChoose != 5) {
            System.out.println("1. Добавить задачу\n2. Показать все задачи\n3. Изменить статус задачи\n4. Удалить задачу\n5. Выйти");
            try {
                String result = input.nextLine();
                cmdChoose = Integer.parseInt(result);

                switch (cmdChoose) {
                    case 1:
                        //Тут начинается веселье с добавлением задачи--------------------------------------------------
                        System.out.println("Введите текст задачи\n");
                        String taskTitle = input.nextLine();

                        if (taskTitle.trim().isEmpty()) {
                            System.out.println("Название задачи не может быть пустым");
                        } else {
                            Tasks task = new Tasks(taskTitle);
                            tasksList.add(task);
                            System.out.println("Задача добавлена.");
                        }

                        break;
                    case 2:

                        int hehehe = tasksList.size();
                        if (hehehe == 0){
                            System.out.println("А задачек-то нет!");
                        } else {
                            for (int i = 0; i < tasksList.size(); i++) {
                                Tasks t = tasksList.get(i); // берём элемент из списка
                                String status = t.getCompleted() ? "[выполнено]" : "[не выполнено]";
                                System.out.println(t.getId() + ". " + t.getTitle() + " Статус: " + status + "\n");
                            }
                        }
                        break;


                    case 3:

                        System.out.println("Введите номер задачи, чтобы сменить её статус");
                        String stringId1 = input.nextLine();
                        try {
                            int numberId = Integer.parseInt(stringId1);
                            for (int i = 0; i < tasksList.size(); i++) {
                                Tasks t = tasksList.get(i);
                                int a = t.getId();
                                boolean b = t.getCompleted();
                                if (a == numberId) {
                                    flag = true;
                                    if (b == false) {
                                        t.setCompleted(true);
                                        System.out.println("Статус задачи " + numberId + " изменён.");
                                    }
                                    if (b == true) {
                                        t.setCompleted(false);
                                        System.out.println("Статус задачи " + numberId + " изменён.");
                                    }
                                }
                            }
                            if (flag == false) {
                                System.out.println("Такого номера задачи нет");
                            }
                        }catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод. Пожалуйста, введите числовой ID задачи.");
                        }

                        break;
                    case 4:
                        System.out.println("Введите номер задачи, чтобы удалить её");
                        String stringId2 = input.nextLine();

                        try {
                            int deleteId = Integer.parseInt(stringId2);
                            //Пробегаемся циклом, ищем задачу и удаляем её
                            for (int i = 0; i < tasksList.size(); i++) {
                                Tasks t = tasksList.get(i);
                                int a = t.getId();
                                if (a == deleteId) {
                                    flag = true;
                                    tasksList.remove(t);
                                    t = null;
                                }
                            }
                            if (flag == false) {
                                System.out.println("Такого номера задачи нет");
                            }
                        }catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод. Пожалуйста, введите числовой ID задачи.");
                        }

                        for (int i = 0; i < tasksList.size(); i++) {
                            Tasks t = tasksList.get(i);
                            t.setId(i+1);
                        }

                        Tasks.setCounter(tasksList.size());


                        break;
                    case 5:
                        System.out.println("Выход...Происходит запись задач...");

                        try(BufferedWriter tasksWriter = new BufferedWriter(new FileWriter("tasks.csv"))){
                            for (int i = 0; i < tasksList.size(); i++) {
                                Tasks t = tasksList.get(i);
                                int a = t.getId();
                                String b = t.getTitle();
                                boolean c = t.getCompleted();
                                tasksWriter.write(a+";"+b+";"+c);
                                tasksWriter.newLine();
                            }

                        }catch (IOException e) {
                            System.err.println("Ошибка: " + e.getMessage());
                        }

                        break;
                    default:
                        System.out.println("Вводить нужно цифры от 1 до 5");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите числовой код команды.");
            }
        }
    }
}