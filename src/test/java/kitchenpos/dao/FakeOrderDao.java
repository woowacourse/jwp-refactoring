package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;

public class FakeOrderDao implements OrderDao {

    private final List<Order> IN_MEMORY_ORDER;

    public FakeOrderDao() {
        IN_MEMORY_ORDER = new ArrayList<>();
    }

    @Override
    public Order save(Order entity) {
        IN_MEMORY_ORDER.add(entity);
        Long id = (long) IN_MEMORY_ORDER.size();
        entity.setId(id);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return IN_MEMORY_ORDER.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(IN_MEMORY_ORDER);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return IN_MEMORY_ORDER.stream()
                .filter(product -> product.getId().equals(orderTableId))
                .anyMatch(product -> orderStatuses.contains(product.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return IN_MEMORY_ORDER.stream()
                .filter(product -> orderTableIds.contains(product.getId()))
                .anyMatch(product -> orderStatuses.contains(product.getOrderStatus()));
    }
}
