import java.time.LocalDate;

public class DiscountProduct extends Product {
    private double sizeDiscount; // Размер скидки в долях (например, 0.20 = 20%)
    private LocalDate expiryDate; // Срок действия скидки

    public DiscountProduct(String nameProduct, int priceProduct, double sizeDiscount, LocalDate expiryDate) {
        super(nameProduct, priceProduct);

        if (sizeDiscount <= 0 || sizeDiscount >= 1) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 1 (например, 0.15 для 15%)");
        }

        if (expiryDate == null) {
            throw new IllegalArgumentException("Нужна дата окончания скидки");
        }

        this.sizeDiscount = sizeDiscount;
        this.expiryDate = expiryDate;
    }

    public double getSizeDiscount() {
        return sizeDiscount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getDiscountedPrice() {
        // Если скидка ещё действует, применяем её
        if (LocalDate.now().isBefore(expiryDate)) {
            return (int) Math.round(getPriceProduct() * (1 - sizeDiscount));
        }
        // Если скидка закончилась, возвращаем полную цену
        return getPriceProduct();
    }

    @Override
    public String toString() {
        return getNameProduct() + " — " +
                (LocalDate.now().isBefore(expiryDate)
                        ? "со скидкой " + (int) (sizeDiscount * 100) + "%: " + getDiscountedPrice() + "₽"
                        : "скидка неактуальна: " + getPriceProduct() + "₽");
    }
}
