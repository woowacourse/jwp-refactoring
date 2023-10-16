package kitchenpos.fake;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> database = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public Order save(Order entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            Order order = new Order(id, entity.getOrderTable(), entity.getOrderStatus(), entity.getOrderedTime(), entity.getOrderLineItems());
            database.put(id, order);
            return order;
        }
        database.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {
        return database.values().stream()
                .anyMatch(it -> it.getOrderTable().getId().equals(orderTableId) &&
                        orderStatuses.contains(it.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {
        return database.values().stream()
                .anyMatch(it -> orderTableIds.contains(it.getOrderTable().getId()) &&
                        orderStatuses.contains(it.getOrderStatus()));
    }
}
