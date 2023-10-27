ALTER TABLE ORDER_LINE_ITEM
    ADD COLUMN name varchar(255);

ALTER TABLE ORDER_LINE_ITEM
    ADD COLUMN price decimal(19, 2);

UPDATE ORDER_LINE_ITEM as oli
SET (oli.name, oli.price) =(
    SELECT m.name, m.price
    FROM MENU as m
    WHERE m.id = oli.menu_id
    )
WHERE EXISTS (
    SELECT 1 FROM menu as m WHERE m.id = oli.menu_id
    );

ALTER TABLE ORDER_LINE_ITEM
DROP menu_id;
