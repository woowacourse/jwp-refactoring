package kitchenpos.dao.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderStatus;

public class FakeOrderDao implements OrderDao {

    private long id = 0L;
    private final Map<Long, Orders> orders = new HashMap<>();

    @Override
    public Orders save(final Orders entity) {
        if (findById(entity.getId()).isPresent()) {
            orders.put(entity.getId(), entity);
            return entity;
        }
        final Orders savedOrder = new Orders(
            ++id, entity.getOrderTableId(), OrderStatus.valueOf(entity.getOrderStatus()), entity.getOrderedTime()
        );
        orders.put(savedOrder.getId(), savedOrder);
        return savedOrder;
    }

    @Override
    public Optional<Orders> findById(final Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Orders> findAll() {
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

    private boolean isEqualsOrderTableId(final Orders order, final Long orderTableId) {
        return order.getOrderTableId().equals(orderTableId);
    }

    private boolean isContainsOrderTableIds(final Orders order, final List<Long> orderTableIds) {
        return orderTableIds.contains(order.getOrderTableId());
    }

    private boolean isContainsOrderStatuses(final Orders order, final List<String> orderStatuses) {
        return orderStatuses.contains(order.getOrderStatus());
    }
}
