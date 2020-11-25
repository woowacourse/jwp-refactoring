package kitchenpos.dao.inmemory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class InmemoryOrderDao implements OrderDao {

    private final Map<Long, Order> orders;
    private long idValue;

    public InmemoryOrderDao() {
        idValue = 0;
        orders = new LinkedHashMap<>();
    }

    @Override
    public Order save(Order entity) {
        long savedId = idValue;
        Order order = new Order();
        order.setId(savedId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(new ArrayList<>(entity.getOrderLineItems()));
        order.setOrderStatus(entity.getOrderStatus());
        order.setOrderTableId(entity.getOrderTableId());
        this.orders.put(savedId, order);
        idValue++;
        return order;
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
                .anyMatch(order -> Objects.equals(order.getOrderTableId(), orderTableId) && orderStatuses.contains(
                        order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orders.values().stream()
                .anyMatch(order -> orderTableIds.contains(order.getOrderTableId()) && orderStatuses.contains(
                        order.getOrderStatus()));
    }

}
