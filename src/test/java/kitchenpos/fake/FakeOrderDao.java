package kitchenpos.fake;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

import java.util.*;

public class FakeOrderDao implements OrderDao {

    private Map<Long, Order> orders = new HashMap<>();
    private Long id = 0L;

    @Override
    public Order save(Order entity) {
        if (entity.getId() != null) {
            orders.put(entity.getId(), entity);
            return entity;
        }
        orders.put(++id, entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return orders.values().stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .filter(order -> orderStatuses.contains(order.getOrderStatus()))
                .findAny()
                .isEmpty();
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orders.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .filter(order -> orderStatuses.contains(order.getOrderStatus()))
                .findAny()
                .isEmpty();
    }
}
