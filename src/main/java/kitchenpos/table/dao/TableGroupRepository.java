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

    public TableGroup save(final TableGroup tableGroup) {
        final TableGroup entity = tableGroupDao.save(tableGroup);
        for (OrderTable orderTable : entity.getOrderTables()) {
            orderTableDao.save(new OrderTable(orderTable.getId(), entity.getId(),
                    orderTable.getNumberOfGuests(), false));
        }
        return tableGroup;
    }
}
