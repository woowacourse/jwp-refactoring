package kitchenpos.dao;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.OrderTableFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.domain.OrderTable;

public class FakeOrderTableDao implements OrderTableDao {

    private static final Map<Long, OrderTable> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public OrderTable save(final OrderTable entity) {
        OrderTable orderTable = generateOrderTable(++id, entity);
        stores.put(id, orderTable);
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(stores.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return stores.values()
                .stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return stores.values()
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId() == tableGroupId)
                .collect(toList());
    }
}
