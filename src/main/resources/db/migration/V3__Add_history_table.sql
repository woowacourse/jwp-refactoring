ALTER TABLE order_line_item DROP constraint fk_order_line_item_menu;

ALTER TABLE order_line_item RENAME COLUMN menu_id TO order_history_id;

CREATE TABLE order_history (
                      id BIGINT(20) NOT NULL AUTO_INCREMENT,
                      menu_name VARCHAR(255) NOT NULL,
                      price DECIMAL(19, 2) NOT NULL,
                      PRIMARY KEY (id)
);

ALTER TABLE order_line_item
    ADD CONSTRAINT fk_order_line_item_order_history
        FOREIGN KEY (order_history_id) REFERENCES order_history (id);
