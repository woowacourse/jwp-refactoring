package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class InMemoryOrderDao implements OrderDao {
    private Map<Long, Order> orders;
    private long index;

    public InMemoryOrderDao() {
        this.orders = new HashMap<>();
        this.index = 0;
    }

    @Override
    public Order save(final Order entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final Order order = new Order();
        order.setId(key);
        order.setOrderTableId(entity.getOrderTableId());
        order.setOrderStatus(entity.getOrderStatus());
        order.setOrderedTime(entity.getOrderedTime());
        order.setOrderLineItems(entity.getOrderLineItems());

        orders.put(key, order);
        return order;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return Optional.of(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
            final List<String> orderStatuses) {
        return orders.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(orderTableId))
                .anyMatch(entry -> orderStatuses.contains(entry.getValue().getOrderStatus()))
                ;
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
            final List<String> orderStatuses) {
        return orders.entrySet()
                .stream()
                .filter(entry -> orderTableIds.contains(entry.getKey()))
                .anyMatch(entry -> orderStatuses.contains(entry.getValue().getOrderStatus()))
                ;
    }
}
