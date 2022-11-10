package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dao.OrderTableDao;

public class FakeOrderTableDao implements OrderTableDao {

    private final List<OrderTable> IN_MEMORY_ORDER_TABLE;
    private Long id;

    public FakeOrderTableDao() {
        IN_MEMORY_ORDER_TABLE = new ArrayList<>();
        id = 1L;
    }

    @Override
    public OrderTable save(OrderTable entity) {
        if (entity.getId() == null) {
            OrderTable orderTable = new OrderTable(id++,
                    entity.getTableGroupId(),
                    entity.getNumberOfGuests(),
                    entity.isEmpty());
            IN_MEMORY_ORDER_TABLE.add(orderTable);
            return orderTable;
        }
        IN_MEMORY_ORDER_TABLE.remove(entity);
        return new OrderTable(entity.getId(), entity.getTableGroupId(), entity.getNumberOfGuests(),
                entity.isEmpty());
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return IN_MEMORY_ORDER_TABLE.stream()
                .filter(orderTable -> orderTable.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(IN_MEMORY_ORDER_TABLE);
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return IN_MEMORY_ORDER_TABLE.stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return IN_MEMORY_ORDER_TABLE.stream()
                .filter(orderTable -> orderTable.getTableGroupId() != null)
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
