ALTER TABLE order_line_item
    ADD COLUMN name VARCHAR(255) NOT NULL DEFAULT 'temp';
ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2) NOT NULL DEFAULT 0;

UPDATE order_line_item o
SET (price, name) = (SELECT m.price, m.name
                        FROM menu m
                        WHERE m.id = o.menu_id);
