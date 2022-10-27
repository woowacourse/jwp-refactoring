package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemDao {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    void update(OrderLineItem entity);
}
