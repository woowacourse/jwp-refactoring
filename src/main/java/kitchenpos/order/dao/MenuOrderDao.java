package kitchenpos.order.dao;

import kitchenpos.order.domain.MenuOrder;

public interface MenuOrderDao {

    MenuOrder getById(final Long id);

    MenuOrder save(MenuOrder orderMenu);
}
