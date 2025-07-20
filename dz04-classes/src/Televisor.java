import java.util.Scanner;

public class Televisor {
    public int inches;
    public String name;
    public int maxSound;

    public Televisor(int inches, String name, int maxSound) {
        this.inches = inches;
        this.name = name;
        this.maxSound = maxSound;
    }

    public static Televisor fromUserInput(Scanner scanner) {
        System.out.print("Введите название телевизора: ");
        String name = scanner.nextLine();

        System.out.print("Введите диагональ (в дюймах): ");
        int inches = scanner.nextInt();

        System.out.print("Введите максимальную громкость: ");
        int maxSound = scanner.nextInt();

        scanner.nextLine(); 

        return new Televisor(inches, name, maxSound);
    }

    public int getInches() {
        return inches;
    }

    public String getName() {
        return name;
    }

    public int getMaxSound() {
        return maxSound;
    }
}