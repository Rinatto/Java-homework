import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String namePerson;
        int summMoney;

        // Ввод покупателя с обработкой ошибок
        try {
            System.out.print("Введите имя покупателя: ");
            namePerson = scanner.nextLine();

            System.out.print("Введите сумму денег: ");
            summMoney = Integer.parseInt(scanner.nextLine());

            // создаём покупателя (тут тоже возможна ошибка)
            Person person = new Person(namePerson, summMoney);

            // Цикл покупки
            while (true) {
                System.out.print("Введите название продукта (или 'END' для завершения): ");
                String productName = scanner.nextLine();
                if (productName.equalsIgnoreCase("END")) break;

                System.out.print("Введите цену продукта: ");
                int price;
                try {
                    price = Integer.parseInt(scanner.nextLine());
                    Product product = new Product(productName, price);

                    if (person.addProduct(product)) {
                        System.out.println("Продукт " + product.getNameProduct() + " добавлен в корзину");
                    } else {
                        System.out.println(person.getName() + " не может позволить себе " + product.getNameProduct());
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Цена должна быть числом. Повторите ввод.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("\n" + person);

        } catch (NumberFormatException e) {
            System.out.println("Сумма должна быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
