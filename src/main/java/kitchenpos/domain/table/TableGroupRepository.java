package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.stereotype.Component;

@Component
public class TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupRepository(TableGroupDao tableGroupDao, OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    public TableGroup save(TableGroup tableGroup) {
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), tableGroup.getOrderTables()))
                .getId();
        List<OrderTable> updatedOrderTables = updateOrderTables(tableGroup, tableGroupId);

        return new TableGroup(tableGroupId, tableGroup.getCreatedDate(), updatedOrderTables);
    }

    private List<OrderTable> updateOrderTables(TableGroup tableGroup, Long tableGroupId) {
        return tableGroup.getOrderTables()
                .stream()
                .map(orderTable -> new OrderTable(
                        orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false))
                .map(orderTableDao::save)
                .collect(Collectors.toList());
    }

    public TableGroup findById(Long tableGroupId) {
        TableGroup savedTableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        List<OrderTable> savedOrderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        return new TableGroup(tableGroupId, savedTableGroup.getCreatedDate(), savedOrderTables);
    }
}
