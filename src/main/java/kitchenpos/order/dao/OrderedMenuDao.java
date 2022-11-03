package kitchenpos.order.dao;

import java.util.List;
import kitchenpos.order.domain.OrderedMenu;

public interface OrderedMenuDao {

    OrderedMenu save(OrderedMenu orderedMenu);

    long countByIdIn(List<Long> ids);
}
