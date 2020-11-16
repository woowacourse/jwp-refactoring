package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class InMemoryOrderLineItemDao implements OrderLineItemDao {
    private Map<Long, OrderLineItem> orderLineItems;
    private long index;

    public InMemoryOrderLineItemDao() {
        this.orderLineItems = new HashMap<>();
        this.index = 0;
    }

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        Long seq = entity.getSeq();

        if (seq == null) {
            seq = index++;
        }

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(entity.getOrderId());
        orderLineItem.setMenuId(entity.getMenuId());
        orderLineItem.setQuantity(entity.getQuantity());

        orderLineItems.put(seq, orderLineItem);
        return orderLineItem;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return Optional.ofNullable(orderLineItems.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return new ArrayList<>(orderLineItems.values());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return orderLineItems.values()
                .stream()
                .filter(orderLineItem -> orderId.equals(orderLineItem.getOrderId()))
                .collect(Collectors.toList())
                ;
    }
}
