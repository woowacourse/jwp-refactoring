ALTER TABLE order_line_item ADD COLUMN menu_name VARCHAR(255);
ALTER TABLE order_line_item ADD COLUMN price DECIMAL(19, 2);

UPDATE order_line_item SET menu_name = 'Unknown', price = 0.00 WHERE menu_name IS NULL OR price IS NULL;

ALTER TABLE order_line_item ALTER COLUMN menu_name SET NOT NULL;
ALTER TABLE order_line_item ALTER COLUMN price SET NOT NULL;

ALTER TABlE order_line_item DROP CONSTRAINT fk_order_line_item_to_menu;
ALTER TABlE order_line_item DROP COLUMN menu_id;
