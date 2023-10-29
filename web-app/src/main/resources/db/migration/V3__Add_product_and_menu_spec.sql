ALTER TABLE menu_product ADD name varchar(255);
ALTER TABLE menu_product ADD price decimal(19, 2);

ALTER TABLE order_line_item ADD name varchar(255) NOT NULL;
ALTER TABLE order_line_item ADD price decimal(19, 2) NOT NULL;
