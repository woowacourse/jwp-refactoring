ALTER TABLE order_line_item CHANGE menu_id order_menu_id BIGINT(20);

CREATE TABLE order_menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);
