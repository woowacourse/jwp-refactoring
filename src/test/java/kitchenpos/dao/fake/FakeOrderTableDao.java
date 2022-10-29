package kitchenpos.dao.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class FakeOrderTableDao implements OrderTableDao {

    private long id = 0L;
    private final Map<Long, OrderTable> orderTables = new HashMap<>();

    @Override
    public OrderTable save(final OrderTable entity) {
        long savedId = ++id;
        final OrderTable savedOrderTable = new OrderTable(
            savedId, entity.getTableGroupId(), entity.getNumberOfGuests(), entity.isEmpty()
        );
        orderTables.put(savedId, savedOrderTable);
        return savedOrderTable;
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
        return orderTables.values().stream()
            .filter(orderTable -> ids.contains(orderTable.getId()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTables.values().stream()
            .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
            .collect(Collectors.toUnmodifiableList());
    }
}
