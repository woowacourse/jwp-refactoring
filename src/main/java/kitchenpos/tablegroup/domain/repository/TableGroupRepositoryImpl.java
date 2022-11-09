package kitchenpos.tablegroup.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupRepositoryImpl(final TableGroupDao tableGroupDao, final OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final TableGroup tableGroup = tableGroupDao.save(entity);
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(entity.getOrderTableIds());

        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            final OrderTable savedOrderTable = orderTableDao.save(
                    new OrderTable(orderTable.getId(), tableGroup.getId(), orderTable.getNumberOfGuests(), false));
            savedOrderTables.add(savedOrderTable);
        }
        return new TableGroup(tableGroup.getId(), tableGroup.getCreatedDate(), new OrderTables(savedOrderTables));
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroupDao.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
