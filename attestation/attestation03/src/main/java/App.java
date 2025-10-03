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
                insertProduct(conn);
                insertCustomer(conn);
                createOrder(conn);
                readOrders(conn);
                updateProduct(conn);
                deleteTestRecords(conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Ошибка транзакции: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertProduct(Connection conn) throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Тестовый товар");
            stmt.setBigDecimal(2, new java.math.BigDecimal("999.99"));
            stmt.setInt(3, 10);
            stmt.setString(4, "Тест");
            stmt.executeUpdate();
        }
    }

    static void insertCustomer(Connection conn) throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Тест");
            stmt.setString(2, "Покупатель");
            stmt.setString(3, "+79990000000");
            stmt.setString(4, "test@example.com");
            stmt.executeUpdate();
        }
    }

    static void createOrder(Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 1); // id продукта (предположим, что есть)
            stmt.setInt(2, 1); // id клиента (предположим, что есть)
            stmt.setInt(3, 1);
            stmt.setInt(4, 1); // статус "Создан"
            stmt.executeUpdate();
        }
    }

    static void readOrders(Connection conn) throws SQLException {
        String sql = """
                SELECT o.id, c.first_name || ' ' || c.last_name AS customer, p.description, o.quantity, o.order_date
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
                System.out.printf("Заказ #%d | %s | %s | %d шт. | %s%n",
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
            stmt.executeUpdate(sql);
        }
    }

    static void deleteTestRecords(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM orders WHERE customer_id = 1");
        conn.createStatement().executeUpdate("DELETE FROM customer WHERE id = 1");
        conn.createStatement().executeUpdate("DELETE FROM product WHERE category = 'Тест'");
    }
}
