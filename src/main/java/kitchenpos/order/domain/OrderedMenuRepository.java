package kitchenpos.order.domain;

import java.util.List;

public interface OrderedMenuRepository {

    OrderedMenu save(OrderedMenu orderedMenu);

    long countByIdIn(List<Long> ids);
}
