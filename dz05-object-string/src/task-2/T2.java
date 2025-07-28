import java.util.Scanner;

public class T2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();

        String arrowRight = ">>-->";
        String arrowLeft = "<--<<";

        int count = 0;

        for (int i = 0; i <= str.length() - 5; i++) {
            String sub = str.substring(i, i + 5);
            if (sub.equals(arrowRight) || sub.equals(arrowLeft)) {
                count++;
            }
        }

        System.out.println("Количество стрел: " + count);

        scanner.close();
    }
}
// Задача 2. Задана последовательность, состоящая только из символов ‘>’,
// ‘<’ и ‘-‘.
// Требуется найти количество стрел, которые спрятаны в этой
// последовательности. Стрелы – это подстроки вида ‘>>-->’ и ‘<--<<’.
// Входные данные: в первой строке входного потока записана строка,
// состоящая из символов ‘>’, ‘<’ и ‘-‘ (без пробелов). Строка может содержать
// до
// 106 символов.

// Выходные данные: в единственную строку выходного потока нужно
// вывести искомое количество стрелок