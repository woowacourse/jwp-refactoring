package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;

public class FakeOrderDao implements OrderDao {

    private final List<Order> IN_MEMORY_ORDER;
    private Long id;

    public FakeOrderDao() {
        IN_MEMORY_ORDER = new ArrayList<>();
        id = 1L;
    }

    @Override
    public Order save(Order entity) {
        Order order = new Order(id++, entity.getOrderTableId(), entity.getOrderStatus(), entity.getOrderedTime());
        IN_MEMORY_ORDER.add(order);
        return order;
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
