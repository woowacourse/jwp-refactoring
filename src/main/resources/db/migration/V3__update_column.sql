ALTER TABLE order_line_item DROP menu_id;

ALTER TABLE order_line_item ADD name VARCHAR(255) NOT NULL;

ALTER TABLE order_line_item ADD price DECIMAL(19, 2) NOT NULL;