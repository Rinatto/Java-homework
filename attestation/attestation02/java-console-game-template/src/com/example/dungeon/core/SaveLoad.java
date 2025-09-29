package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SaveLoad {
    private static final Path SAVE = Paths.get("save.txt");
    private static final Path SCORES = Paths.get("scores.csv");

    public static void save(GameState s) {
        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {
            Player p = s.getPlayer();
            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getAttack());
            w.newLine();
            String inv = p.getInventory().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName())
                    .collect(Collectors.joining(","));
            w.write("inventory;" + inv);
            w.newLine();
            w.write("room;" + s.getCurrent().getName());
            w.newLine();
            System.out.println("Сохранено в " + SAVE.toAbsolutePath());
            writeScore(p.getName(), s.getScore());
            for (Room room : s.getWorld()) {
                Monster m = room.getMonster();
                if (m != null && m.getHp() > 0) {
                    String lootStr = m.getLoot().stream()
                            .map(i -> i.getClass().getSimpleName() + ":" + i.getName())
                            .collect(Collectors.joining(","));
                    w.write("monster;" + room.getName() + ";" + m.getName() + ";" + m.getLevel() + ";" + m.getHp() + ";"
                            + lootStr);
                    w.newLine();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    public static void load(GameState s) {
        if (!Files.exists(SAVE)) {
            System.out.println("Сохранение не найдено.");
            return;
        }

        try (BufferedReader r = Files.newBufferedReader(SAVE)) {
            Map<String, List<String>> map = new HashMap<>();
            for (String line; (line = r.readLine()) != null;) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    map.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
                }
            }

            Player p = s.getPlayer();

            // Загрузка игрока
            List<String> playerLines = map.get("player");
            if (playerLines != null && !playerLines.isEmpty()) {
                String[] pp = playerLines.get(0).split(";");
                if (pp.length >= 3) {
                    p.setName(pp[0]);
                    p.setHp(Integer.parseInt(pp[1]));
                    p.setAttack(Integer.parseInt(pp[2]));
                } else {
                    System.out.println("⚠️ Повреждённая строка player: " + playerLines.get(0));
                }
            }

            // Загрузка инвентаря
            p.getInventory().clear();
            List<String> inventoryLines = map.get("inventory");
            if (inventoryLines != null && !inventoryLines.isEmpty()) {
                String inv = inventoryLines.get(0);
                if (!inv.isBlank()) {
                    for (String tok : inv.split(",")) {
                        String[] t = tok.split(":", 2);
                        if (t.length < 2)
                            continue;
                        switch (t[0]) {
                            case "Potion" -> p.getInventory().add(new Potion(t[1], 5));
                            case "Key" -> p.getInventory().add(new Key(t[1]));
                            case "Weapon" -> p.getInventory().add(new Weapon(t[1], 3));
                            case "Gold" -> {
                                try {
                                    int amount = Integer.parseInt(t[1].replaceAll("[^0-9]", ""));
                                    p.getInventory().add(new Gold(amount));
                                } catch (NumberFormatException e) {
                                    System.out.println("⚠️ Невозможно прочитать количество золота: " + t[1]);
                                }
                            }
                            default -> System.out.println("Неизвестный предмет: " + t[0]);
                        }
                    }
                }
            }

            // Установка текущей комнаты
            List<String> roomLines = map.get("room");
            if (roomLines != null && !roomLines.isEmpty()) {
                String roomName = roomLines.get(0);
                for (Room room : s.getWorld()) {
                    if (room.getName().equalsIgnoreCase(roomName)) {
                        s.setCurrent(room);
                        break;
                    }
                }
            }

            // Загрузка монстров
            List<String> monsterLines = map.get("monster");
            if (monsterLines != null) {
                for (String line : monsterLines) {
                    String[] parts = line.split(";", 5); // roomName;monsterName;level;hp;lootStr
                    if (parts.length >= 5) {
                        String roomName = parts[0];
                        String monsterName = parts[1];
                        int level = Integer.parseInt(parts[2]);
                        int hp = Integer.parseInt(parts[3]);
                        String lootStr = parts[4];

                        List<Item> loot = new ArrayList<>();
                        if (!lootStr.isBlank()) {
                            for (String tok : lootStr.split(",")) {
                                String[] t = tok.split(":", 2);
                                if (t.length < 2)
                                    continue;
                                switch (t[0]) {
                                    case "Potion" -> loot.add(new Potion(t[1], 5));
                                    case "Key" -> loot.add(new Key(t[1]));
                                    case "Weapon" -> loot.add(new Weapon(t[1], 3));
                                    case "Gold" -> {
                                        try {
                                            int amount = Integer.parseInt(t[1].replaceAll("[^0-9]", ""));
                                            loot.add(new Gold(amount));
                                        } catch (NumberFormatException e) {
                                            System.out.println(
                                                    "⚠️ Невозможно прочитать количество золота в луте: " + t[1]);
                                        }
                                    }
                                }
                            }
                        }

                        // Найти комнату и установить монстра, если hp > 0
                        for (Room room : s.getWorld()) {
                            room.setMonster(null);
                            if (room.getName().equalsIgnoreCase(roomName)) {
                                Monster monster = new Monster(monsterName, level, hp, loot);
                                room.setMonster(monster);
                                break;
                            }
                        }
                    }
                }
            }

            System.out.println("Игра загружена.");
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    public static void printScores() {
        if (!Files.exists(SCORES)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES);
            try (BufferedWriter w = Files.newBufferedWriter(SCORES, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score");
                    w.newLine();
                }
                w.write(LocalDateTime.now() + "," + player + "," + score);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score) {
    }
}
