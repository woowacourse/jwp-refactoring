ALTER TABLE menu_product
    ADD COLUMN name VARCHAR(255) NOT NULL DEFAULT 'temp';
ALTER TABLE menu_product
    ADD COLUMN price DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE menu_product m
SET (price, name) = (SELECT p.price, p.name FROM product p WHERE p.id = m.product_id);
