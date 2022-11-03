ALTER TABLE order_line_item ADD menu_name VARCHAR(255);
ALTER TABLE order_line_item ADD menu_price BIGINT(20);

INSERT INTO order_line_item (menu_name, menu_price)
SELECT menu.name, menu.price
FROM menu JOIN order_line_item
    ON menu.id = order_line_item.menu_id;

ALTER TABLE order_line_item DROP COLUMN menu_id;
