package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));
        commands.put("move", (ctx, a) -> {
            if (a.isEmpty())
                throw new InvalidCommandException("Укажите направление: north, south, east или west");

            String direction = a.getFirst().toLowerCase();
            Room next = ctx.getCurrent().getNeighbors().get(direction);

            if (next == null) {
                throw new InvalidCommandException("Нет выхода в направлении: " + direction);
            }

            ctx.setCurrent(next);
            System.out.println("Вы перешли в: " + next.getName());
            System.out.println(next.describe());
        });
        commands.put("take", (ctx, args) -> {
            if (args.isEmpty()) {
                throw new InvalidCommandException("Укажите название предмета.");
            }

            String itemName = String.join(" ", args);
            Room currentRoom = ctx.getCurrent();

            Optional<Item> foundItem = currentRoom.getItems().stream()
                    .filter(item -> item.getName().equalsIgnoreCase(itemName))
                    .findFirst();

            if (foundItem.isEmpty()) {
                throw new InvalidCommandException("Предмет не найден в комнате: " + itemName);
            }

            Item item = foundItem.get();
            currentRoom.getItems().remove(item);
            ctx.getPlayer().getInventory().add(item);
            System.out.println("Взят предмет: " + item.getName());
        });
        commands.put("inventory", (ctx, a) -> {
            List<Item> items = ctx.getPlayer().getInventory();

            if (items.isEmpty()) {
                System.out.println("Инвентарь пуст.");
                return;
            }

            // Группируем по имени класса предмета (типу), сортируем по имени
            Map<String, List<Item>> grouped = items.stream()
                    .sorted(Comparator.comparing(Item::getName))
                    .collect(Collectors.groupingBy(it -> it.getClass().getSimpleName()));

            grouped.forEach((type, list) -> {
                System.out.println("- " + type + " (" + list.size() + "):");
                for (Item item : list) {
                    System.out.println("  • " + item.getName());
                }
            });
        });
        commands.put("use", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Что использовать? Введите: use <имя предмета>");
            }

            String name = String.join(" ", a);
            List<Item> inventory = ctx.getPlayer().getInventory();

            Optional<Item> optItem = inventory.stream()
                    .filter(it -> it.getName().equalsIgnoreCase(name))
                    .findFirst();

            if (optItem.isEmpty()) {
                throw new InvalidCommandException("Предмет не найден в инвентаре: " + name);
            }

            Item item = optItem.get();
            item.apply(ctx); // Полиморфный вызов

            inventory.remove(item); // Удаляем одноразовый предмет
        });
        commands.put("fight", (ctx, a) -> {
            Room current = ctx.getCurrent();
            Monster monster = current.getMonster();

            if (monster == null || monster.getHp() <= 0) {
                throw new InvalidCommandException("В комнате нет живого монстра.");
            }

            Player player = ctx.getPlayer();
            System.out.println("Бой начался с монстром: " + monster.getName());

            while (player.getHp() > 0 && monster.getHp() > 0) {
                // Игрок атакует
                monster.setHp(monster.getHp() - player.getAttack());
                System.out.println("Вы бьёте " + monster.getName() + " на " + player.getAttack() + ". HP монстра: "
                        + Math.max(monster.getHp(), 0));

                if (monster.getHp() <= 0) {
                    System.out.println("Монстр побеждён!");
                    // Лут
                    if (!monster.getLoot().isEmpty()) {
                        System.out.println("Получено:");
                        for (Item item : monster.getLoot()) {
                            System.out.println("- " + item.getName());
                            ctx.getPlayer().getInventory().add(item);
                        }
                    }
                    // Удаляем монстра
                    current.setMonster(null);
                    return;
                }

                // Монстр атакует в ответ
                player.setHp(player.getHp() - monster.getDamage());
                System.out.println(monster.getName() + " отвечает на " + monster.getDamage() + ". Ваше HP: "
                        + Math.max(player.getHp(), 0));
            }

            if (player.getHp() <= 0) {
                System.out.println("Вы погибли. Игра окончена.");
                System.exit(0); // Завершаем игру
            }
        });

        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });
    }

    private void initRooms() {
        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);
        forest.getItems().add(new Potion("Малое зелье", 5));

        state.setWorld(List.of(square, forest, cave));
        state.setCurrent(square);
    }

    private void initMonsters() {
        for (Room room : state.getWorld()) {
            if (room.getName().equals("Лес")) {
                List<Item> loot = new ArrayList<>();
                loot.add(new Gold(10));
                loot.add(new Potion("Клык зверя", 3));
                room.setMonster(new Monster("Волк", 1, 8, loot));
            }
        }
    }

    private void bootstrapWorld() {
    Player hero = new Player("Герой", 20, 5);
    state.setPlayer(hero);

    initRooms();

    if (java.nio.file.Files.exists(java.nio.file.Paths.get("save.txt"))) {
        System.out.print("Найдено сохранение. Загрузить его? (y/n): ");
        // Читаем без закрытия System.in
        try {
            // Поток System.in нельзя закрывать, подавляем предупреждение
            @SuppressWarnings("resource")
            String answer = new Scanner(System.in).nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("д")) {
                SaveLoad.load(state);
                return;
            }
        } catch (Exception e) {
            System.err.println("Ошибка при попытке загрузки сохранения: " + e.getMessage());
        }
        
    }

    initMonsters();
}
    // private void bootstrapWorld() {
    // Player hero = new Player("Герой", 20, 5);
    // state.setPlayer(hero);

    // Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
    // Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
    // Room cave = new Room("Пещера", "Темно и сыро.");
    // square.getNeighbors().put("north", forest);
    // forest.getNeighbors().put("south", square);
    // forest.getNeighbors().put("east", cave);
    // cave.getNeighbors().put("west", forest);

    // forest.getItems().add(new Potion("Малое зелье", 5));
    // List<Item> loot = new ArrayList<>();
    // loot.add(new Gold(10));
    // loot.add(new Potion("Клык зверя", 3));
    // Monster wolf = new Monster("Волк", 1, 8, loot);
    // forest.setMonster(wolf);
    // state.setCurrent(square);

    // List<Room> allRooms = List.of(square, forest, cave);
    // state.setWorld(allRooms);
    // }

    public void run() {
        System.out.println("DungeonMini (TEMPLATE). 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null)
                    break;
                line = line.trim();
                if (line.isEmpty())
                    continue;
                List<String> parts = Arrays.asList(line.split("\s+"));
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null)
                        throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out
                            .println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
