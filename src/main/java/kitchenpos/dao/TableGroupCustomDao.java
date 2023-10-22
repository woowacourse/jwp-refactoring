package kitchenpos.dao;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupCustomDao {

    private final TableGroupDao tableGroupDao;

    private final OrderTableDao orderTableDao;

    public TableGroupCustomDao(final TableGroupDao tableGroupDao, final OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }

    public TableGroup save(TableGroup entity) {
        final Long tableGroupId = tableGroupDao
                .save(new TableGroup(entity.getId(), entity.getCreatedDate(), List.of()))
                .getId();

        final List<OrderTable> savedOrderTables = entity.getOrderTables()
                .stream()
                .map(orderTable ->
                        orderTableDao.save(
                                new OrderTable(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false))
                )
                .collect(Collectors.toList());

        return new TableGroup(tableGroupId, entity.getCreatedDate(), savedOrderTables);
    }
}
