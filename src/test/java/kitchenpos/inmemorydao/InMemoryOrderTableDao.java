package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class InMemoryOrderTableDao implements OrderTableDao {
    private Map<Long, OrderTable> orderTables;
    private long index;

    public InMemoryOrderTableDao() {
        this.orderTables = new HashMap<>();
        this.index = 0;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final OrderTable orderTable = new OrderTable();
        orderTable.setId(key);
        orderTable.setTableGroupId(entity.getTableGroupId());
        orderTable.setNumberOfGuests(entity.getNumberOfGuests());
        orderTable.setEmpty(entity.isEmpty());

        orderTables.put(key, orderTable);
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(orderTables.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return ids.stream()
                .map(orderTables::get)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTables.values()
                .stream()
                .filter(orderTable -> tableGroupId.equals(orderTable.getTableGroupId()))
                .collect(Collectors.toList())
                ;
    }
}
