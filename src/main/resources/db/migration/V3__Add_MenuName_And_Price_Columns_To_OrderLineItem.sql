ALTER TABLE order_line_item
    ADD COLUMN name VARCHAR(255);

ALTER TABLE order_line_item
    ADD COLUMN price DECIMAL(19, 2);

UPDATE order_line_item AS oi
SET (oi.name, oi.price) = (SELECT m.name, m.price FROM menu as m WHERE m.id = oi.menu_id);

ALTER TABLE order_line_item
DROP CONSTRAINT fk_order_line_item_to_menu;

ALTER TABLE order_line_item
DROP COLUMN menu_id;
