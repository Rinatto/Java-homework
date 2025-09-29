package com.example.dungeon.model;

public class Gold extends Item {
    private final int amount;

    public Gold(int amount) {
        super("Золото"); // имя по умолчанию
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    // Переопределяем метод, чтобы показать количество
    @Override
    public String getName() {
        return super.getName() + " (" + amount + " монет)";
    }

    @Override
    public void apply(GameState ctx) {
        // Логика применения: добавляем золото игроку
        ctx.getPlayer().addGold(amount); 
        System.out.println("Вы получили " + amount + " монет!");
    }
}