import java.util.*;

public class T3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите два слова через пробел:");

        String str = scanner.nextLine().toLowerCase();
        System.out.println("Строка в нижнем регистре:" + str);

        String[] words = str.split(" ");
        if (words.length != 2) {
            System.out.println("Ошибка, требуется ввести ровно два слова");
            scanner.close();
            return;
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            System.out.println("Текущее слово перед сортировкой: " + word);

            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sortedWord = new String(chars);

            System.out.println("Отсортированное слово: " + sortedWord);
            result.append(sortedWord);

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        System.out.println("Результат: " + result.toString());
        scanner.close();
    }
}

// Задача 3*. Задана строка, состоящая из букв английского алфавита,
// разделенных одним пробелом. Необходимо каждую последовательность
// символов упорядочить по возрастанию и вывести слова в нижнем регистре.
// Входные данные: в единственной строке последовательность символов
// представляющее два слова.
// Выходные данные: упорядоченные по возрастанию буквы в нижнем
// регистре