package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class InmemoryOrderLineItemDao implements OrderLineItemDao {

    private final Map<Long, OrderLineItem> orderLineItems;
    private long idValue;

    public InmemoryOrderLineItemDao() {
        idValue = 0;
        orderLineItems = new LinkedHashMap<>();
    }

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        long savedId = idValue;
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(savedId);
        orderLineItem.setMenuId(orderLineItem.getMenuId());
        orderLineItem.setOrderId(orderLineItem.getOrderId());
        orderLineItem.setQuantity(orderLineItem.getQuantity());
        this.orderLineItems.put(savedId, orderLineItem);
        idValue++;
        return orderLineItem;
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
                .filter(item -> Objects.equals(orderId, item.getOrderId()))
                .collect(Collectors.toList());
    }
}
