package kitchenpos.fake;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryOrderLineItemDao implements OrderLineItemDao {
    private final Map<Long, OrderLineItem> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        long id = this.id.getAndIncrement();
        entity.setSeq(id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return map.values().stream()
                .filter(it -> Objects.nonNull(it.getOrderId()))
                .filter(it -> it.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}
