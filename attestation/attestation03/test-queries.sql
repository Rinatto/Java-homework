-- 5 запросов на чтение (SELECT)
-- 1. Список всех заказов за последние 7 дней с именем клиента и описанием товара
SELECT o.id, o.order_date, c.first_name, c.last_name, p.description, s.name AS status
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status s ON o.status_id = s.id
WHERE o.order_date >= NOW() - INTERVAL '7 days'
ORDER BY o.order_date DESC;

-- 2. Топ-3 самых популярных товара по количеству заказов
SELECT p.description, SUM(o.quantity) AS total_sold
FROM orders o
JOIN product p ON o.product_id = p.id
GROUP BY p.description
ORDER BY total_sold DESC
LIMIT 3;

-- 3. Список клиентов с количеством их заказов
SELECT c.first_name, c.last_name, COUNT(o.id) AS order_count
FROM customer c
LEFT JOIN orders o ON c.id = o.customer_id
GROUP BY c.id
ORDER BY order_count DESC;

-- 4. Все заказы со статусом "Доставлен"
SELECT o.id, c.first_name || ' ' || c.last_name AS customer_name, o.order_date, p.description
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status s ON o.status_id = s.id
WHERE s.name = 'Доставлен';

-- 5. Средняя стоимость товара по категориям
SELECT category, AVG(price) AS avg_price
FROM product
GROUP BY category
ORDER BY avg_price DESC;

-- 3 запроса на обновление (UPDATE)
-- 6. Обновить статус заказа с id = 1 на "Доставлен"
UPDATE orders SET status_id = (
  SELECT id FROM order_status WHERE name = 'Доставлен'
)
WHERE id = 1;

-- 7. Изменить email клиента с id = 2
UPDATE customer
SET email = 'anna.petrova@newmail.com'
WHERE id = 2;

-- 8. Уменьшить количество товаров на складе после покупки (например, заказ с id = 3)
UPDATE product
SET quantity = quantity - (
  SELECT quantity FROM orders WHERE id = 3
)
WHERE id = (
  SELECT product_id FROM orders WHERE id = 3
);

--2 запроса на удаление (DELETE)
-- 9. Удалить все заказы со статусом "Отменен"
DELETE FROM orders
WHERE status_id = (
  SELECT id FROM order_status WHERE name = 'Отменен'
);

-- 10. Удалить клиентов, у которых нет ни одного заказа
DELETE FROM customer
WHERE id NOT IN (
  SELECT DISTINCT customer_id FROM orders
);
