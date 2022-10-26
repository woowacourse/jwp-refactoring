package kitchenpos.util;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderDao implements OrderDao {

    private Long id = 0L;
    private final Map<Long, Order> repository = new HashMap<>();

    @Override
    public Order save(Order entity) {
        if (entity.getId() == null) {
            entity.setId(++id);
            repository.put(entity.getId(), entity);
            return entity;
        }
        repository.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return repository.values()
                .stream()
                .anyMatch(each -> Objects.equals(each.getOrderTableId(), orderTableId) &&
                        orderStatuses.contains(each.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return repository.values()
                .stream()
                .anyMatch(each -> orderTableIds.contains(each.getOrderTableId()) &&
                        orderStatuses.contains(each.getOrderStatus()));
    }
}
