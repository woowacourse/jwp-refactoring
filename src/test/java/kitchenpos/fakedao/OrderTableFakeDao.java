package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class OrderTableFakeDao implements OrderTableDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, OrderTable> repository = new HashMap<>();

    @Override
    public OrderTable save(OrderTable entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return ids.stream()
                .map(repository::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return repository.values()
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
