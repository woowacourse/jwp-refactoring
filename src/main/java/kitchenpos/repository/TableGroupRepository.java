package kitchenpos.repository;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupRepository(OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroup create(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateOrderTableIds(orderTableIds, savedOrderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        updateOrderTable(savedOrderTables, savedTableGroup.getId());

        return savedTableGroup;
    }

    private void validateOrderTableIds(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new NotFoundOrderTableException();
        }
    }

    private void updateOrderTable(List<OrderTable> savedOrderTables, Long tableGroupId) {
        for (OrderTable savedOrderTable : savedOrderTables) {
            orderTableDao.save(new OrderTable(savedOrderTable, tableGroupId));
        }
    }

    public void ungroup(List<OrderTable> orderTables) {
        for (OrderTable savedOrderTable : orderTables) {
            savedOrderTable.ungroup();
            orderTableDao.save(savedOrderTable);
        }
    }
}
