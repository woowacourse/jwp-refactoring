CREATE TABLE menu_history (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    menu_id BIGINT(20) NOT NULL,
    menu_group_id BIGINT(20) NOT NULL,
    created_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE menu_history
    ADD CONSTRAINT fk_menu_history_menu
        FOREIGN KEY (menu_id) REFERENCES menu (id);

ALTER TABLE menu_history
    ADD CONSTRAINT fk_menu_history_menu_group
        FOREIGN KEY (menu_group_id) REFERENCES menu_group (id);
