package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private final List<OrderLineItem> IN_MEMORY_ORDER_LINE_ITEM;

    public FakeOrderLineItemDao() {
        this.IN_MEMORY_ORDER_LINE_ITEM = new ArrayList<>();
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        IN_MEMORY_ORDER_LINE_ITEM.add(entity);
        Long seq = (long) IN_MEMORY_ORDER_LINE_ITEM.size();
        entity.setSeq(seq);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return IN_MEMORY_ORDER_LINE_ITEM.stream()
                .filter(orderLineItem -> orderLineItem.getSeq().equals(id))
                .findFirst();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return new ArrayList<>(IN_MEMORY_ORDER_LINE_ITEM);
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return IN_MEMORY_ORDER_LINE_ITEM.stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}
