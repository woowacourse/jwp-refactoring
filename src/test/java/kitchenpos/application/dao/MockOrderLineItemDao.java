package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class MockOrderLineItemDao implements OrderLineItemDao {

    private Map<Long, OrderLineItem> orderLineItems = new HashMap<>();
    private Long id = 1L;

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        if (Objects.isNull(entity.getSeq())) {
            entity.setSeq(id++);
        }
        orderLineItems.put(entity.getSeq(), entity);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.ofNullable(orderLineItems.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return new ArrayList<>(orderLineItems.values());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItems.values().stream()
            .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
            .collect(Collectors.toList());
    }
}
