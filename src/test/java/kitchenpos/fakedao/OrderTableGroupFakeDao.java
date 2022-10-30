package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.OrderTableGroup;

public class OrderTableGroupFakeDao implements OrderTableGroupDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, OrderTableGroup> repository = new HashMap<>();

    @Override
    public OrderTableGroup save(OrderTableGroup entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<OrderTableGroup> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<OrderTableGroup> findAll() {
        return new ArrayList<>(repository.values());
    }
}
