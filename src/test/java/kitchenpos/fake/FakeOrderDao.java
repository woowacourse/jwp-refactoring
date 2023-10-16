package kitchenpos.fake;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeOrderDao implements OrderDao {

    private Map<Long, Order> orders = new HashMap<>();
    private Long id = 0L;

    @Override
    public Order save(Order entity) {
        if (entity.getId() != null) {
            orders.put(entity.getId(), entity);
            return entity;
        }
        entity.setId(++id);
        entity.setOrderedTime(LocalDateTime.now());
        orders.put(id, entity);
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
                .filter(order -> order.getOrderTable().getId().equals(orderTableId))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orders.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTable().getId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
