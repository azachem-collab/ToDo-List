package org.example;

import java.util.ArrayList;
import java.util.Scanner;

class Tasks {
    private int id;
    private String title;
    private boolean completed;
    private static int counter = 0;

    public Tasks(String taskTitle) {
        this.title = taskTitle;
        this.id = ++counter;
        this.completed = false;
    }

    //Сеттеры
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
        int cmdChoose = 0;
        ArrayList<Tasks> tasksList = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        while (cmdChoose != 4) {
            System.out.println("1. Добавить задачу\n2. Показать все задачи\n3. Изменить статус задачи\n4. Выйти");
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
                        for (int i = 0; i < tasksList.size(); i++) {
                            Tasks t = tasksList.get(i); // берём элемент из списка
                            String status = t.getCompleted() ? "[выполнено]" : "[не выполнено]";
                            System.out.println(t.getId() + ". " + t.getTitle() + " Статус: " + status + "\n");
                        }
                        break;
                    case 3:
                        boolean flag = false;
                        System.out.println("Введите номер задачи, чтобы сменить её статус");
                        String stringId = input.nextLine();
                        try {
                            int numberId = Integer.parseInt(stringId);
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
                        System.out.println("Выход...");
                        break;
                    default:
                        System.out.println("Вводить нужно цифры от 1 до 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите числовой код команды.");
            }
        }
    }
}