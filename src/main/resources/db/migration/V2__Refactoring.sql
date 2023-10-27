-- 추가된 열 생성
-- ALTER TABLE order_line_item DROP COLUMN orderId BIGINT;

ALTER TABLE order_line_item ADD COLUMN name varchar(255) not null;
ALTER TABLE order_line_item ADD COLUMN price  decimal(19, 2) not null;

ALTER TABLE order_line_item DROP CONSTRAINT fk_order_line_item_to_menu;
ALTER TABLE order_line_item DROP COLUMN menu_id;

