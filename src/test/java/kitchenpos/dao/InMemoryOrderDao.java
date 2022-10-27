package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import kitchenpos.domain.Order;

public class InMemoryOrderDao implements OrderDao {

    protected final AtomicLong id;
    protected final Map<Long, Order> database;

    public InMemoryOrderDao() {
        this.id = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }

    public InMemoryOrderDao(final AtomicLong id, final Map<Long, Order> database) {
        this.id = id;
        this.database = database;
    }

    @Override
    public Order save(final Order entity) {
        long entityId = checkEntityIdCondition(entity);
        entity.setId(entityId);
        database.put(entityId, entity);
        return entity;
    }

    private long checkEntityIdCondition(final Order entity) {
        if (Objects.nonNull(entity.getId())) {
            return entity.getId();
        }
        return id.getAndIncrement();
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return database.values()
                .stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return database.values()
                .stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
