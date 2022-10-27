package kitchenpos.dao;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class InMemoryOrderLineItemDao extends InMemoryAbstractDao<OrderLineItem> implements OrderLineItemDao {

    @Override
    protected BiConsumer<OrderLineItem, Long> setId() {
        return OrderLineItem::setSeq;
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return database.values()
                .stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}
