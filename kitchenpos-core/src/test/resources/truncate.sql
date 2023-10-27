SET REFERENTIAL_INTEGRITY FALSE;

-- 테이블 데이터 삭제
DELETE FROM menu_product;
DELETE FROM order_line_item;
DELETE FROM menu;
DELETE FROM menu_group;
DELETE FROM orders;
DELETE FROM order_table;
DELETE FROM product;
DELETE FROM table_group;

-- 외래 키 제약 조건을 다시 활성화
SET REFERENTIAL_INTEGRITY TRUE;
