package kitchenpos.fakedao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFakeDao implements OrderLineItemDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, OrderLineItem> repository = new HashMap<>();

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setSeq(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return null;
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return null;
    }
}
