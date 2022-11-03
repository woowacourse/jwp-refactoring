CREATE TABLE orders (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_table_id BIGINT(20) NOT NULL,
    order_status VARCHAR(255) NOT NULL,
    ordered_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_line_item (
     seq BIGINT(20) NOT NULL AUTO_INCREMENT,
     order_id BIGINT(20) NOT NULL,
     menu_id BIGINT(20) NOT NULL,
     quantity BIGINT(20) NOT NULL,
     PRIMARY KEY (seq)
);
