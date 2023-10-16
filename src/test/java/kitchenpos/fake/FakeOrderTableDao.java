package kitchenpos.fake;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.table.OrderTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeOrderTableDao implements OrderTableDao {

    private Map<OrderTable, OrderTable> orderTables = new HashMap<OrderTable, OrderTable>();
    private OrderTable id = 0L;

    @Override
    public OrderTable save(OrderTable entity) {
        if (entity.getId() != null) {
            orderTables.put(entity.getId(), entity);
            return entity;
        }
        entity.setId(++id);
        orderTables.put(id, entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(OrderTable id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(orderTables.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTables.values().stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTables.values().stream()
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
