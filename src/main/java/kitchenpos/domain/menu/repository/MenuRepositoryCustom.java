package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findAllByOrder(Order order);
}
