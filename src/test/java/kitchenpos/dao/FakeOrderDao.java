package kitchenpos.dao;

import static kitchenpos.application.fixture.OrderFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.domain.Order;

public class FakeOrderDao implements OrderDao {

    private static final Map<Long, Order> STORES = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Order save(final Order entity) {
        Order order = generateOrder(++id, entity);
        STORES.put(id, order);
        return order;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return Optional.of(STORES.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(STORES.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return STORES.values()
                .stream()
                .filter(order -> order.getOrderTableId() == orderTableId)
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return STORES.values()
                .stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
