package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;

import java.util.List;

public interface OrderLineItemDao {

    OrderLineItem save(final OrderLineItem entity);

    List<OrderLineItem> findAll();
}
