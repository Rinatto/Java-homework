import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class App {
    public static void main(String[] args) {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            props.load(input);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false); // транзакция

            try {
                System.out.println("Подключение к базе данных установлено");

                int productId = insertProduct(conn);
                int customerId = insertCustomer(conn);
                createOrder(conn, productId, customerId);

                readOrders(conn);
                updateProduct(conn);
                deleteTestRecords(conn, productId, customerId);

                conn.commit();
                System.out.println("Все операции успешно выполнены и зафиксированы (COMMIT)");
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Ошибка транзакции, выполнен rollback: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static int insertProduct(Connection conn) throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Тестовый товар");
            stmt.setBigDecimal(2, new java.math.BigDecimal("999.99"));
            stmt.setInt(3, 10);
            stmt.setString(4, "Тест");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Добавлен тестовый товар с ID: " + id);
                return id;
            } else {
                throw new SQLException("Не удалось получить ID добавленного товара");
            }
        }
    }

    static int insertCustomer(Connection conn) throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Тест");
            stmt.setString(2, "Покупатель");
            stmt.setString(3, "+79990000000");
            stmt.setString(4, "test@example.com");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Добавлен тестовый покупатель с ID: " + id);
                return id;
            } else {
                throw new SQLException("Не удалось получить ID покупателя");
            }
        }
    }

    static void createOrder(Connection conn, int productId, int customerId) throws SQLException {
        String sql = "INSERT INTO orders (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, customerId);
            stmt.setInt(3, 1);
            stmt.setInt(4, 1); // статус "Создан"
            int rows = stmt.executeUpdate();
            System.out.println("Создан заказ (строк вставлено: " + rows + ")");
        }
    }

    static void readOrders(Connection conn) throws SQLException {
        String sql = """
                SELECT o.id, c.first_name || ' ' || c.last_name AS customer, 
                       p.description, o.quantity, o.order_date
                FROM orders o
                JOIN customer c ON o.customer_id = c.id
                JOIN product p ON o.product_id = p.id
                ORDER BY o.order_date DESC
                LIMIT 5
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Последние 5 заказов:");
            while (rs.next()) {
                System.out.printf("➡ Заказ #%d | %s | %s | %d шт. | %s%n",
                        rs.getInt("id"),
                        rs.getString("customer"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("order_date"));
            }
        }
    }

    static void updateProduct(Connection conn) throws SQLException {
        String sql = "UPDATE product SET price = price + 100 WHERE category = 'Тест'";
        try (Statement stmt = conn.createStatement()) {
            int updated = stmt.executeUpdate(sql);
            System.out.println("Обновлено товаров категории 'Тест': " + updated);
        }
    }

    static void deleteTestRecords(Connection conn, int productId, int customerId) throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM orders WHERE customer_id = " + customerId);
        conn.createStatement().executeUpdate("DELETE FROM customer WHERE id = " + customerId);
        conn.createStatement().executeUpdate("DELETE FROM product WHERE id = " + productId);
        System.out.println("Удалены тестовые записи из всех таблиц");
    }
}
