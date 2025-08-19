import java.util.ArrayList;
import java.util.List;

public class Person {
    private String namePerson;
    private int summMoney;
    private List<Product> products = new ArrayList<>();

    public Person(String namePerson, int summMoney) {
        if (namePerson == null || namePerson.trim().length() < 3) {
            throw new IllegalArgumentException("Имя должно содержать минимум 3 символа");
        }
        if (summMoney < 0) {
            throw new IllegalArgumentException("Сумма денег не может быть отрицательной");
        }
        this.namePerson = namePerson;
        this.summMoney = summMoney;
    }

    public String getName() {
        return namePerson;

    }

    public int getSummMoney() {
        return summMoney;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean addProduct(Product product) {
    int priceToPay;

    // Проверяем, скидочный ли продукт
    if (product instanceof DiscountProduct) {
        priceToPay = ((DiscountProduct) product).getDiscountedPrice();
    } else {
        priceToPay = product.getPriceProduct();
    }

    // Проверка достаточности средств
    if (priceToPay <= summMoney) {
        products.add(product);
        summMoney -= priceToPay;
        return true;
    } else {
        return false;
    }
}

    @Override
    public String toString() {
        if (products.isEmpty()) {
            return namePerson + " — ничего не купил";
        }

        StringBuilder sb = new StringBuilder(namePerson + " — купил: ");
        for (Product product : products) {
            sb.append(product).append(", "); // вызывается product.toString()
        }
        sb.setLength(sb.length() - 2); // удаляем последнюю запятую и пробел
        sb.append(". Осталось: ").append(summMoney).append("₽");

        return sb.toString();
    }

}
// Характеристики Покупателя: имя, сумма денег и пакет с продуктами

// (массив объектов типа Продукт). Имя не может быть пустой строкой и не может
// быть короче 3 символов. Деньги не могут быть отрицательным числом.

// Если Покупатель может позволить себе Продукт, то Продукт добавляется
// в пакет. Если у Покупателя недостаточно денег, то добавление не происходит.
