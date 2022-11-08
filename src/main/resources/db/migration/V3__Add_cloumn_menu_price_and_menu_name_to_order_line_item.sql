ALTER TABLE order_line_item
    ADD COLUMN menu_price DECIMAL(19, 2) NOT NULL;

ALTER TABLE order_line_item
    ADD COLUMN menu_name VARCHAR(255) NOT NULL;

UPDATE order_line_item
SET menu_price = (SELECT price FROM menu where id = menu_id),
    menu_name  = (SELECT name FROM menu where id = menu_id);