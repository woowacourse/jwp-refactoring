INSERT INTO table_group (created_date) VALUES (now());
INSERT INTO order_table (table_group_id, number_of_guests, empty) VALUES (1, 0, false); -- 1번 테이블, 테이블 그룹 존재
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true); -- 2번 테이블, 테이블 그룹 존재 x, order 1에 속함
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true); -- 3번 테이블, 테이블 그룹 존재 x, order 2에 속함
INSERT INTO order_table (number_of_guests, empty) VALUES (0, true); -- 4번 테이블, 테이블 그룹 존재 x, order 3에 속함

INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (2, 'COOKING', now());
INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (3, 'MEAL', now());
INSERT INTO orders (order_table_id, order_status, ordered_time) VALUES (4, 'COMPLETION', now());
