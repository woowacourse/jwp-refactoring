package kitchenpos.fake;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryOrderDao implements OrderDao {

    private final Map<Long, Order> database = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public Order save(Order entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            entity.setId(id);
            database.put(id, entity);
            return entity;
        }
        update(entity);
        return entity;
    }

    private void update(Order entity) {
        Order order = database.get(entity.getId());
        order.setOrderStatus(entity.getOrderStatus());
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
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return database.values().stream()
                .anyMatch(it -> it.getOrderTableId().equals(orderTableId) &&
                        orderStatuses.contains(it.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return database.values().stream()
                .anyMatch(it -> orderTableIds.contains(it.getOrderTableId()) &&
                        orderStatuses.contains(it.getOrderStatus()));
    }
}
