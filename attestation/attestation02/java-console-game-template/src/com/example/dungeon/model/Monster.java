package com.example.dungeon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Monster extends Entity {
    private int level;
    private List<Item> loot = new ArrayList<>();

    public Monster(String name, int level, int hp, List<Item> loot) {
        super(name, hp);
        this.level = level;
        this.loot = loot;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDamage() {
        return new Random().nextInt(level * 3) + 1;
    }

     public List<Item> getLoot() {
        return loot;
    }

     public void setLoot(List<Item> loot) {
        this.loot = loot;
    }

    public void addLoot(Item item) {
        this.loot.add(item);
    }
}
