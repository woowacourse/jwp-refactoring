package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private final List<OrderLineItem> IN_MEMORY_ORDER_LINE_ITEM;
    private Long seq;

    public FakeOrderLineItemDao() {
        this.IN_MEMORY_ORDER_LINE_ITEM = new ArrayList<>();
        seq = 1L;
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        OrderLineItem orderLineItem = new OrderLineItem(seq++,
                entity.getOrderId(),
                entity.getMenuId(),
                entity.getQuantity());
        IN_MEMORY_ORDER_LINE_ITEM.add(orderLineItem);
        return orderLineItem;
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
