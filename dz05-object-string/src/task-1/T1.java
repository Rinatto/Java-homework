import java.util.Scanner;

public class T1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String keyString = "qwertyuiopasdfghjklzxcvbnm";

        System.out.print("Введите букву: ");
        String key = scanner.nextLine().toLowerCase();

        int index = keyString.indexOf(key);

        if (index == -1) {
            System.out.println("Ошибка: введённый символ не найден в раскладке.");
        } else {
            
            int leftIndex = (index - 1 + keyString.length()) % keyString.length();
            char leftChar = keyString.charAt(leftIndex);

            System.out.println("Буква слева: " + leftChar);
        }

        scanner.close();
    }
}

// Задача 1. Для введенной с клавиатуры буквы английского алфавита

// нужно вывести слева стоящую букву на стандартной клавиатуре. При этом
// клавиатура замкнута, т.е. справа от буквы «p» стоит буква «a», а слева от "а"
// буква "р", также соседними считаются буквы «l» и буква «z», а буква «m» с
// буквой «q».

// Входные данные: строка входного потока содержит один символ —
// маленькую букву английского алфавита.

// Выходные данные: следует вывести букву стоящую слева от заданной
// буквы, с учетом замкнутости клавиатуры