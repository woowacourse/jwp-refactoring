package kitchenpos.application.dao;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private final Map<Long, OrderLineItem> orderLineItems = new HashMap<>();

    private long id = 1;

    @Override
    public OrderLineItem save(final OrderLineItem orderLineItem) {
        orderLineItem.setSeq(id);
        orderLineItems.put(id++, orderLineItem);
        return orderLineItem;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.ofNullable(orderLineItems.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItems.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItems.values()
                .stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        orderLineItems.clear();
    }
}
