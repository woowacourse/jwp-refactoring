ALTER TABLE order_line_item
ADD COLUMN menu_name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
ADD COLUMN menu_price DECIMAL(19, 2) NOT NULL;

UPDATE order_line_item
SET (menu_name, menu_price) = (
    SELECT m.name, m.price
    FROM menu m
    WHERE menu_id = m.id
    );
