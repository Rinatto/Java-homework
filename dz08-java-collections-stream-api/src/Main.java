import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(
                new Car("a123me", "Mercedes", "White", 0, 8300000),
                new Car("b873of", "Volga", "Black", 0, 673000),
                new Car("w487mn", "Lexus", "Grey", 76000, 900000),
                new Car("p987hj", "Volga", "Red", 610, 704340),
                new Car("c987ss", "Toyota", "White", 254000, 761000),
                new Car("o983op", "Toyota", "Black", 698000, 740000),
                new Car("p146op", "BMW", "White", 271000, 850000),
                new Car("u893ii", "Toyota", "Purple", 210900, 440000),
                new Car("l097df", "Toyota", "Black", 108000, 780000),
                new Car("y876wd", "Toyota", "Black", 160000, 1000000));

        // 1) Цвет или пробег = 0
        String colorToFind = "Black";
        int mileageToFind = 0;

        List<String> matchingNumbers = cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumCar)
                .collect(Collectors.toList());

        System.out.println("Номера автомобилей по цвету или пробегу:");
        matchingNumbers.forEach(System.out::println);

        // 2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
        int minPrice = 700_000;
        int maxPrice = 800_000;

        long uniqueModelsCount = cars.stream()
                .filter(car -> car.getPrice() >= minPrice && car.getPrice() <= maxPrice)
                .map(Car::getModel)
                .distinct()
                .count();

        System.out.println("Уникальные модели в ценовом диапазоне: " + uniqueModelsCount);

        // 3) Цвет автомобиля с минимальной стоимостью
        String colorOfMinPriceCar = cars.stream()
                .min((c1, c2) -> Integer.compare(c1.getPrice(), c2.getPrice()))
                .map(Car::getColor)
                .orElse("не найдено");

        System.out.println("Цвет самого дешевого автомобиля: " + colorOfMinPriceCar);

        // 4) Средняя стоимость искомой модели
        String modelToFind = "Toyota";

        double avgToyotaPrice = cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(modelToFind))
                .mapToInt(Car::getPrice)
                .average()
                .orElse(0.0);

        System.out.printf("Средняя стоимость %s: %.2f%n", modelToFind, avgToyotaPrice);

    }

}

// 1) Номера всех автомобилей, имеющих заданный в переменной цвет
// colorToFind или нулевой пробег mileageToFind.
// 2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
// 3) Вывести цвет автомобиля с минимальной стоимостью.
// 4) Среднюю стоимость искомой модели modelToFind