package kitchenpos.fake;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryOrderLineItemRepository implements OrderLineItemRepository {
    private final Map<Long, OrderLineItem> map = new HashMap<>();
    private final AtomicLong seq = new AtomicLong();

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        if (Objects.isNull(entity.getSeq())) {
            long seq = this.seq.getAndIncrement();
            OrderLineItem orderLineItem = new OrderLineItem(seq, entity.getMenuId(), entity.getQuantity());
            map.put(seq, orderLineItem);
            return orderLineItem;
        }
        map.put(entity.getSeq(), entity);
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
}
