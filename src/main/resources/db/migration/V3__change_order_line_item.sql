ALTER TABLE order_line_item
    ADD menu_name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item
    ADD menu_price DECIMAL(19, 2) NOT NULL;

UPDATE order_line_item
    SET menu_name = (SELECT name FROM menu WHERE id = menu_id);

UPDATE order_line_item
    SET menu_price = (SELECT price FROM menu WHERE id = menu_id);

ALTER TABLE order_line_item DROP menu_id;
