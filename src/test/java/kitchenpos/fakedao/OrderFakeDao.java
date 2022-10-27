package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class OrderFakeDao implements OrderDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, Order> repository = new HashMap<>();

    @Override
    public Order save(Order entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return repository.values()
                .stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return repository.values()
                .stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }
}
