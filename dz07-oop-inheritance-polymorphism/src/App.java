import java.util.Scanner;
import java.time.LocalDate;

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
                System.out.print("Введите тип продукта (1 — обычный, 2 — скидочный, 'END' — завершить): ");
                String type = scanner.nextLine();

                if (type.equalsIgnoreCase("END"))
                    break;

                System.out.print("Введите название продукта: ");
                String productName = scanner.nextLine();

                System.out.print("Введите цену продукта: ");
                int price;
                try {
                    price = Integer.parseInt(scanner.nextLine());

                    Product product;

                    if (type.equals("2")) {
                        System.out.print("Введите скидку (например, 0.2 для 20%): ");
                        double discount = Double.parseDouble(scanner.nextLine());

                        System.out.print("Введите срок действия скидки (ГГГГ-ММ-ДД): ");
                        LocalDate expiryDate = LocalDate.parse(scanner.nextLine());

                        product = new DiscountProduct(productName, price, discount, expiryDate);
                    } else {
                        product = new Product(productName, price);
                    }

                    int finalPrice = (product instanceof DiscountProduct)
                            ? ((DiscountProduct) product).getDiscountedPrice()
                            : product.getPriceProduct();

                    if (finalPrice <= person.getSummMoney()) {
                        person.addProduct(product);
                        System.out.println("Продукт " + product + " добавлен в корзину");
                    } else {
                        System.out.println(person.getName() + " не может позволить себе " + product.getNameProduct());
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: цена или скидка должна быть числом. Повторите ввод.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            System.out.println("\n" + person);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: сумма должна быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        scanner.close();
    }
}