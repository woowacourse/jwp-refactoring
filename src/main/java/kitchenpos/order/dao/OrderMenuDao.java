package kitchenpos.order.dao;

import kitchenpos.order.domain.OrderMenu;

public interface OrderMenuDao {

    OrderMenu getById(final Long id);

    OrderMenu save(OrderMenu orderMenu);

    OrderMenu getByMenuId(Long menuId);
}
