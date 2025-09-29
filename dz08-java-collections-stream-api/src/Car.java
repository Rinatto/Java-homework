public class Car {
    protected String numCar;
    protected String model;
    protected String color;
    protected int mileage;
    protected int price;

    public Car(String numCar, String model, String color, int mileage, int price) {
        this.numCar = numCar;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
    }

    public String getNumCar() {
        return this.numCar;
    }

    public String getModel() {
        return this.model;
    }

    public String getColor() {
        return this.color;
    }

    public int getMileage() {
        return this.mileage;
    }

    public int getPrice() {
        return this.price;
    }

    public void setNumCar(String numCar) {
        this.numCar = numCar;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return numCar + " " + model + " " + color + " " + mileage + " " + price;
    }

}
