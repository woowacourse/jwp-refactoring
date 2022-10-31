ALTER TABLE order_line_item DROP COLUMN menu_id;
ALTER TABLE order_line_item ADD COLUMN name VARCHAR(255) not null;
ALTER TABLE order_line_item ADD COLUMN price DECIMAL(19, 2) not null;
