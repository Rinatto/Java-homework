public class Main {
    public static void main(String[] args) {
        int vasyliy = (int) (Math.random() * 3);
        int petr = (int) (Math.random() * 3);

        System.out.println("Василий: " + vasyliy);
        System.out.println("Петр: " + petr);

        if (vasyliy == petr) {
            System.out.println("Ничья");
        } else if ((vasyliy == 0 && petr == 1) || (vasyliy == 1 && petr == 2) || (vasyliy == 2 && petr == 0)) {
            System.out.println("Василий победил");
        } else {
            System.out.println("Петр победил");
        }

    }
}
// Задача 2*. Вася и Петя играют в игру “Камень, ножницы, бумага”.
// Каждый из них показывает свою фигуру камень-0, ножницы-1, бумага-2.
// Программа определяет, кто из них выиграл.
// Выбор каждого участника формируется случайным образом.