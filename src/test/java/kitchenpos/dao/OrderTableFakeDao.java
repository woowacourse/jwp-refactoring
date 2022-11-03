package kitchenpos.dao;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;

public class OrderTableFakeDao extends BaseFakeDao<OrderTable> implements OrderTableDao {

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return entities.values()
                .stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return entities.values()
                .stream()
                .filter(orderTable -> orderTable.getTableGroupId() != null)
                .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
