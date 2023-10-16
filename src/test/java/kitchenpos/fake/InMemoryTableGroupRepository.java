package kitchenpos.fake;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTableGroupRepository implements TableGroupRepository {

    private final Map<Long, TableGroup> map = new HashMap<>();
    private final Map<Long, OrderTable> orderTableMap = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);
    private final AtomicLong orderTableId = new AtomicLong(0);

    @Override
    public TableGroup save(TableGroup entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            TableGroup tableGroup = new TableGroup(id, entity.getCreatedDate(), saveAll(entity.getOrderTables()));
            map.put(id, tableGroup);
            return tableGroup;
        }
        map.put(entity.getId(), entity);
        return entity;
    }

    private List<OrderTable> saveAll(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(this::saveOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable saveOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable.getId())) {
            long id = this.orderTableId.getAndIncrement();
            OrderTable entity = new OrderTable(id, orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
            orderTableMap.put(id, entity);
            return entity;
        }
        orderTableMap.put(orderTable.getId(), orderTable);
        return orderTable;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(map.values());
    }
}
