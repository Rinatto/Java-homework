import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String s = scanner.nextLine().toLowerCase();
        String t = scanner.nextLine().toLowerCase();

        String sortedStringS = Stream.of(s.split("")).sorted().collect(Collectors.joining()).toLowerCase();
        String sortedStringT = Stream.of(t.split("")).sorted().collect(Collectors.joining()).toLowerCase();

        if (sortedStringS.equals(sortedStringT)) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        scanner.close();
    }
}

// Задание 2:
// С консоли на вход подается две строки s и t. Необходимо вывести true, если
// одна
// строка является валидной анаграммой другой строки, и false – если это не так.
// Анаграмма – это слово, или фраза, образованная путем перестановки букв
// другого
// слова или фразы, обычно с использованием всех исходных букв ровно один раз.
// Для проверки:
// Бейсбол – бобслей
// ● Героин – регион
// ● Клоака – околка