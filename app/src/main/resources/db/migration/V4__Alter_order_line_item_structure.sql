ALTER TABLE order_line_item
    ADD COLUMN price decimal(19, 2);

ALTER TABLE order_line_item
    ADD COLUMN name varchar(255);

UPDATE order_line_item
SET price = (SELECT m.price FROM menu m WHERE order_line_item.menu_id = m.id),
    name = (SELECT m.name FROM menu m WHERE order_line_item.menu_id = m.id)
WHERE EXISTS (SELECT 1 FROM menu m WHERE order_line_item.menu_id = m.id);


ALTER TABLE order_line_item
DROP CONSTRAINT fk_order_line_item_to_menu;

ALTER TABLE order_line_item
DROP COLUMN MENU_ID;

ALTER TABLE order_line_item
    ALTER COLUMN price SET NOT NULL;

ALTER TABLE order_line_item
    ALTER COLUMN name SET NOT NULL;

