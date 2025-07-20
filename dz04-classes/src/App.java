import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Televisor tv1 = new Televisor(32, "Samsung", 80);
        Televisor tv2 = new Televisor(43, "LG", 90);

        System.out.println("Создание телевизора вручную:");
        Televisor tv3 = Televisor.fromUserInput(scanner);

        System.out.println(tv1.getName() + " — " + tv1.getInches() + " дюймов, громкость до " + tv1.getMaxSound());
        ;
        System.out.println(tv2.getName() + " — " + tv2.getInches() + " дюймов, громкость до " + tv2.getMaxSound());
        ;
        System.out.println(tv3.getName() + " — " + tv3.getInches() + " дюймов, громкость до " + tv3.getMaxSound());
        ;
    }
}

// Формулировка задания:
// Реализовать класс Телевизор. У класса есть поля, свойства и методы.
// Проверить работу в классе App, методе main.
// Ожидаемый результат:
// 1. Создан класс Телевизор;
// 2. У класса есть поля, свойства и методы. Поля желательно сделать
// 3. Задать новые значения полям класса можно через конструктор.
// 4. Создан класс App с методом main.
// 5. В методе main класса App создано несколько экземпляров класса
// Телевизор.
// 6. Дополнительно. Задавать параметры класса Телевизор с клавиатуры
// или случайным числом