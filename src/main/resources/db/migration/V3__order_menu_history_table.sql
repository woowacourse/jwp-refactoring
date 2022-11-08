ALTER TABLE order_line_item ADD name varchar(255) NOT NULL;
ALTER TABLE order_line_item ADD price DECIMAL(19, 2) NOT NULL;
ALTER TABLE order_line_item DROP COLUMN menu_id;
