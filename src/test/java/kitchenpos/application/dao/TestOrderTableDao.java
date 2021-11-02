package kitchenpos.application.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class TestOrderTableDao extends TestAbstractDao<OrderTable> implements OrderTableDao {

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return database.values().stream()
            .filter(orderTable -> ids.contains(orderTable.getId()))
            .collect(toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return database.values().stream()
            .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
            .collect(toList());
    }

    @Override
    protected BiConsumer<OrderTable, Long> setIdConsumer() {
        return OrderTable::setId;
    }

    @Override
    protected Comparator<OrderTable> comparatorForSort() {
        return Comparator.comparingLong(OrderTable::getId);
    }
}
