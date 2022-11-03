ALTER TABLE order_line_item ADD name VARCHAR(255) NOT NULL DEFAULT 'x';
ALTER TABLE order_line_item ADD price DECIMAL(19, 2) NOT NULL DEFAULT 0.00;

UPDATE order_line_item oli SET oli.name = (SELECT name FROM menu m where m.id = oli.menu_id);
UPDATE order_line_item oli SET oli.price = (SELECT price FROM menu m where m.id = oli.menu_id);
