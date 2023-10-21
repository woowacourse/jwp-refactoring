package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.ordertable.OrderTable;

public class InMemoryOrderTableDao implements OrderTableDao {

    private final List<OrderTable> orderTables = new ArrayList<>();

    @Override
    public OrderTable save(final OrderTable entity) {
        entity.setId((long) (orderTables.size() + 1));
        orderTables.add(entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return orderTables.stream()
                          .filter(orderTable -> orderTable.getId().equals(id))
                          .findAny();
    }

    @Override
    public List<OrderTable> findAll() {
        return orderTables;
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return orderTables.stream()
                          .filter(orderTable -> ids.contains(orderTable.getId()))
                          .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTables.stream()
                          .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                          .collect(Collectors.toList());
    }
}
