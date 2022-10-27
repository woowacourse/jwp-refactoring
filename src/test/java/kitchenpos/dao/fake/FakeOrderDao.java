package kitchenpos.dao.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class FakeOrderDao implements OrderDao {

    private long id = 0L;
    private final Map<Long, Order> orders = new HashMap<>();

    @Override
    public Order save(final Order entity) {
        long savedId = ++id;
        orders.put(savedId, entity);
        entity.setId(savedId);
        return entity;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return List.copyOf(orders.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orders.values().stream()
            .anyMatch(order ->
                isEqualsOrderTableId(order, orderTableId) && isContainsOrderStatuses(order, orderStatuses)
            );
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses) {
        return orders.values().stream()
            .anyMatch(order ->
                isContainsOrderTableIds(order, orderTableIds) && isContainsOrderStatuses(order, orderStatuses)
            );
    }

    private boolean isEqualsOrderTableId(final Order order, final Long orderTableId) {
        return order.getOrderTableId().equals(orderTableId);
    }

    private boolean isContainsOrderTableIds(final Order order, final List<Long> orderTableIds) {
        return orderTableIds.contains(order.getOrderTableId());
    }

    private boolean isContainsOrderStatuses(final Order order, final List<String> orderStatuses) {
        return orderStatuses.contains(order.getOrderStatus());
    }
}
