package kitchenpos.util;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private Long id = 0L;
    private final Map<Long, OrderLineItem> repository = new HashMap<>();

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        if (entity.getSeq() == null) {
            OrderLineItem addEntity = new OrderLineItem(
                    ++id, entity.getOrderId(), entity.getMenuId(), entity.getQuantity());
            repository.put(addEntity.getSeq(), addEntity);
            return addEntity;
        }
        return repository.computeIfAbsent(entity.getSeq(), (id) -> entity);
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return repository.values()
                .stream()
                .filter(each -> Objects.equals(each.getOrderId(), orderId))
                .collect(Collectors.toUnmodifiableList());
    }
}
