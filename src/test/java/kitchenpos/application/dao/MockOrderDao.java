package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class MockOrderDao implements OrderDao {

    private Map<Long, Order> orders = new HashMap<>();
    private Long id = 1L;

    @Override
    public Order save(Order entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        orders.put(entity.getId(), entity);
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
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId,
        List<String> orderStatuses) {
        return orders.keySet().stream()
            .filter(id -> id.equals(orderTableId))
            .map(id -> orders.get(id))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
        List<String> orderStatuses) {
        return orders.keySet().stream()
            .filter(orderTableIds::contains)
            .map(id -> orders.get(id))
            .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
