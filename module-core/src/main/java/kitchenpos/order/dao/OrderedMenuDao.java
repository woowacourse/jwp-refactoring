package kitchenpos.order.dao;

import kitchenpos.order.domain.OrderedMenu;

import java.util.List;

public interface OrderedMenuDao {

    OrderedMenu save(OrderedMenu entity);

    long countByIdIn(List<Long> ids);
}
