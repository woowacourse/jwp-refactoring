package kitchenpos.table.dao;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupRepository(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroup save(TableGroup tableGroup) {
        TableGroup entity = tableGroupDao.save(tableGroup);
        final Long tableGroupId = entity.getId();

        for (OrderTable orderTable : entity.getOrderTables()) {
            OrderTable savedOrderTable = new OrderTable(orderTable.getId(), tableGroupId,
                    orderTable.getNumberOfGuests(), false);
            orderTableDao.save(savedOrderTable);
        }
        return entity;
    }
}
