package kitchenpos.service.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class TestOrderLineItemDao extends TestAbstractDao<OrderLineItem> implements
    OrderLineItemDao {

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return database.values().stream()
            .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
            .collect(toList());
    }

    @Override
    protected BiConsumer<OrderLineItem, Long> setIdConsumer() {
        return OrderLineItem::setSeq;
    }

    @Override
    protected Comparator<OrderLineItem> comparatorForSort() {
        return Comparator.comparingLong(OrderLineItem::getOrderId);
    }
}
