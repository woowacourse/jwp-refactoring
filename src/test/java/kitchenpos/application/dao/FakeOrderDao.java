package kitchenpos.application.dao;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeOrderDao implements OrderDao {

    private final Map<Long, Order> orders = new HashMap<>();

    private long id = 1;

    @Override
    public Order save(final Order order) {
        order.setId(id);
        orders.put(id++, order);
        return order;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return orders.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orders.values()
                .stream()
                .anyMatch(order -> order.getOrderTableId().equals(orderTableId)
                        && orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orders.values()
                .stream()
                .anyMatch(order -> orderTableIds.contains(order.getOrderTableId())
                        && orderStatuses.contains(order.getOrderStatus()));
    }

    public void clear() {
        orders.clear();
    }
}
