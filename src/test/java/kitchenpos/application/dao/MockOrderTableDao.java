package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class MockOrderTableDao implements OrderTableDao {

    private Map<Long, OrderTable> orderTables = new HashMap<>();
    private Long id = 1L;

    @Override
    public OrderTable save(OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        orderTables.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(orderTables.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTables.keySet().stream()
            .filter(ids::contains)
            .map(id -> orderTables.get(id))
            .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTables.values().stream()
            .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
            .collect(Collectors.toList());
    }
}
