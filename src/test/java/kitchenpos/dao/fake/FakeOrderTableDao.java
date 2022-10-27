package kitchenpos.dao.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class FakeOrderTableDao implements OrderTableDao {

    private long id = 0L;
    private final Map<Long, OrderTable> orderTables = new HashMap<>();

    @Override
    public OrderTable save(final OrderTable entity) {
        long savedId = ++id;
        orderTables.put(savedId, entity);
        entity.setId(savedId);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return List.copyOf(orderTables.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables.values()) {
            if (ids.contains(orderTable.getId())) {
                savedOrderTables.add(orderTable);
            }
        }
        return savedOrderTables;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables.values()) {
            if (orderTable.getTableGroupId().equals(tableGroupId)) {
                savedOrderTables.add(orderTable);
            }
        }
        return savedOrderTables;
    }
}
