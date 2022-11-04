package kitchenpos.order.domain;

import java.util.List;

public interface OrderLineItemDao {

    OrderLineItem save(final OrderLineItem entity);

    List<OrderLineItem> findAll();
}
