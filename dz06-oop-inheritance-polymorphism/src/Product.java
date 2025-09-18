public class Product {
    private String nameProduct;
    private int priceProduct;

    public Product(String nameProduct, int priceProduct) {
        if (nameProduct == null || nameProduct.trim().length() < 1) {
            throw new IllegalArgumentException("Hазвание продукта должно быть");
        }
        if (priceProduct < 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательным числом");
        }
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

     @Override
    public String toString() {
        return nameProduct + " (" + priceProduct + "₽)";
    }

}
// Характеристики Продукта: название и стоимость. Название продукта не
// может быть пустой строкой, оно должно быть. Стоимость продукта не может
// быть отрицательным числом.
