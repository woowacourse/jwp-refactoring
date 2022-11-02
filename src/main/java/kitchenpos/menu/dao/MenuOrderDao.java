package kitchenpos.menu.dao;

import kitchenpos.menu.domain.MenuOrder;

public interface MenuOrderDao {

    MenuOrder getById(final Long id);

    MenuOrder save(MenuOrder orderMenu);
}
