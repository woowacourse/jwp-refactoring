package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class InmemoryOrderTableDao implements OrderTableDao {

    private final Map<Long, OrderTable> orderTables;
    private long idValue;

    public InmemoryOrderTableDao() {
        idValue = 0;
        orderTables = new LinkedHashMap<>();
    }

    @Override
    public OrderTable save(OrderTable entity) {
        long savedId = idValue;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(savedId);
        orderTable.setEmpty(entity.isEmpty());
        orderTable.setNumberOfGuests(entity.getNumberOfGuests());
        orderTable.setTableGroupId(entity.getTableGroupId());
        this.orderTables.put(savedId, orderTable);
        idValue++;
        return orderTable;
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
        return orderTables.values().stream()
                .filter(item -> ids.contains(item.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTables.values().stream()
                .filter(item -> Objects.equals(item.getTableGroupId(), tableGroupId))
                .collect(Collectors.toList());
    }
}
