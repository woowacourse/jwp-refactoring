package kitchenpos.util;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.table.OrderTable;

import java.util.*;
import java.util.stream.Collectors;

public class FakeOrderTableDao implements OrderTableDao {

    private Long id = 0L;
    private final Map<Long, OrderTable> repository = new HashMap<>();

    @Override
    public OrderTable save(OrderTable entity) {
        if (entity.getId() == null) {
            OrderTable addEntity = new OrderTable(++id, entity.getTableGroupId(),
                    entity.getNumberOfGuests(), entity.isEmpty());
            repository.put(addEntity.getId(), addEntity);
            return addEntity;
        }
        repository.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<OrderTable> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return repository.values()
                .stream()
                .filter(each -> ids.contains(each.getId()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return repository.values()
                .stream()
                .filter(each -> Objects.equals(each.getTableGroupId(), tableGroupId))
                .collect(Collectors.toUnmodifiableList());
    }
}
