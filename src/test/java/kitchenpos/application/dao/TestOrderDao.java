package kitchenpos.application.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class TestOrderDao implements OrderDao {

    protected final AtomicLong incrementId;
    protected final Map<Long, Order> database;

    public TestOrderDao() {
        this.incrementId = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }

    public TestOrderDao(AtomicLong incrementId, Map<Long, Order> database) {
        this.incrementId = incrementId;
        this.database = database;
    }

    public Order save(Order entity) {
        long entityId =
            Objects.nonNull(entity.getId()) ?
                entity.getId() :
                incrementId.getAndIncrement();
        entity.setId(entityId);
        database.put(entityId, entity);
        return entity;

    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<Order> findAll() {
        return database.values().stream()
            .sorted(Comparator.comparingLong(Order::getId))
            .collect(toList());
    }

    public long countByIdIn(List<Long> ids) {
        return ids.stream()
            .filter(database::containsKey)
            .count();
    }

    public boolean existsById(Long id) {
        return database.containsKey(id);
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId,
                                                        List<String> orderStatuses) {
        return database.values().stream()
            .filter(order -> order.getOrderTableId().equals(orderTableId))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
                                                          List<String> orderStatuses) {
        return database.values().stream()
            .filter(order -> orderTableIds.contains(order.getOrderTableId()))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

}
