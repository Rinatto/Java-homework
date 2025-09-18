import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {
    public static void main(String[] args) {

        HashSet<Integer> set1 = new HashSet<Integer>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        set2.add(4);

        System.out.println(intersection(set1, set2));
        System.out.println(union(set1, set2));
        System.out.println(relativeComplement(set1, set2));
    }

    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        for (T el : set1) {
            if (set2.contains(el)) {
                result.add(el);
            }
        }
        return result;
    }

    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    public static <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

}
// Задание 3:
// Реализовать класс PowerfulSet, в котором должны быть следующие методы:
// ● public <T> Set<T> intersection(Set<T> set1, Set<T> set2) – возвращает
// пересечение двух наборов.
// Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {1, 2}
// ● public <T> Set<T> union(Set<T> set1, Set<T> set2) – возвращает
// объединение двух наборов
// Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {0, 1, 2, 3, 4}
// ● public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) –
// возвращает элементы первого набора без тех, которые находятся также и
// во втором наборе.
// Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {3}