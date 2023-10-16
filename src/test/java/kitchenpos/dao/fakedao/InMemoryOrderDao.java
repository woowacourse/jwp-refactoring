package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class InMemoryOrderDao implements OrderDao {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public Order save(final Order entity) {
        entity.setId((long) (orders.size() + 1));
        orders.add(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return orders.stream()
                     .filter(order -> order.getId().equals(id))
                     .findAny();
    }

    @Override
    public List<Order> findAll() {
        return orders;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return existsByOrderTableIdInAndOrderStatusIn(List.of(orderTableId), orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses) {
        return orders.stream()
                     .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                     .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
