import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    public class Main {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        words.add("Orange");
        words.add("Green");
        words.add("Orange");
        words.add("Yellow");
        words.add("Orange");
        words.add("Black");
        words.add("Orange");
        words.add("Black");
        words.add("Orange");

        System.out.println(getUniqueElements(words));
    }

    public static <T> Set<T> getUniqueElements(List<T> list) {
        return new HashSet<>(list);
    }
}

// Задание 1:
// Реализовать метод, который на вход принимает ArrayList<T>, а возвращает набор
// уникальных элементов этого массива. Решить, используя коллекции