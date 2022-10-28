package kitchenpos.application.dao;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeOrderTableDao implements OrderTableDao {

    private final Map<Long, OrderTable> orderTables = new HashMap<>();

    private long id = 1;

    @Override
    public OrderTable save(final OrderTable orderTable) {
        orderTable.setId(id);
        orderTables.put(id++, orderTable);
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return orderTables.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return orderTables.entrySet()
                .stream()
                .filter(entry -> ids.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTables.values()
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        orderTables.clear();
    }
}
