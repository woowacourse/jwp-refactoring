CREATE TABLE order_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    menu_id BIGINT(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    primary key (id)
);

ALTER TABLE order_line_item drop constraint fk_order_line_item_menu;
ALTER TABLE order_line_item CHANGE menu_id order_menu_id BIGINT(20);
