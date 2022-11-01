package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;

@Service
public class TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupRepository(final TableGroupDao tableGroupDao, final OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    public TableGroup save(final TableGroup entity) {
        final TableGroup savedTableGroup = tableGroupDao.save(entity)
                .toEntity();

        final List<OrderTable> orderTables = entity.getOrderTables();
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(savedTableGroup.getId());
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }

        savedTableGroup.setOrderTables(orderTables);
        return savedTableGroup;
    }
}
