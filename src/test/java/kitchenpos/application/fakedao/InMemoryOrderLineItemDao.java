package kitchenpos.application.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class InMemoryOrderLineItemDao implements OrderLineItemDao {

    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        entity.setSeq((long) (orderLineItems.size() + 1));
        orderLineItems.add(entity);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return orderLineItems.stream()
                             .filter(orderLineItem -> orderLineItem.getSeq().equals(id))
                             .findAny();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItems;
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return orderLineItems.stream()
                             .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderId))
                             .collect(Collectors.toList());
    }
}
