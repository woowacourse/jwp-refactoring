insert into order_menu (menu_id, name, price)
select m.id menu_id, m.name name, m.price price
from menu m
         left join order_line_item oli on oli.menu_id = m.id
where m.id is not null
  and oli.seq is not null;

