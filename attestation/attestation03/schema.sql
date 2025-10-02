-- -----------------------------
-- СХЕМА БАЗЫ ДАННЫХ ДЛЯ УЧЕТА ЗАКАЗОВ
-- -----------------------------

-- === Таблица: order_status ===
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON TABLE order_status IS 'Справочник статусов заказов';
COMMENT ON COLUMN order_status.name IS 'Название статуса заказа';

-- === Таблица: product ===
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    
    description TEXT NOT NULL,
    price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    category VARCHAR(100)
);

COMMENT ON TABLE product IS 'Товары';
COMMENT ON COLUMN product.description IS 'Описание товара';
COMMENT ON COLUMN product.price IS 'Стоимость товара';
COMMENT ON COLUMN product.quantity IS 'Доступное количество';
COMMENT ON COLUMN product.category IS 'Категория товара';

-- === Таблица: customer ===
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100)
);

COMMENT ON TABLE customer IS 'Клиенты';
COMMENT ON COLUMN customer.first_name IS 'Имя клиента';
COMMENT ON COLUMN customer.last_name IS 'Фамилия клиента';
COMMENT ON COLUMN customer.phone IS 'Телефон клиента';
COMMENT ON COLUMN customer.email IS 'Email клиента';

-- === Таблица: order ===
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    customer_id INT NOT NULL REFERENCES customer(id) ON DELETE CASCADE,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL CHECK (quantity > 0),
    status_id INT NOT NULL REFERENCES order_status(id)
);

COMMENT ON TABLE orders IS 'Заказы';
COMMENT ON COLUMN orders.product_id IS 'FK на товар';
COMMENT ON COLUMN orders.customer_id IS 'FK на клиента';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.quantity IS 'Количество товаров';
COMMENT ON COLUMN orders.status_id IS 'Статус заказа (FK)';

-- === Индексы ===
CREATE INDEX IF NOT EXISTS idx_orders_product ON orders(product_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status_id);
