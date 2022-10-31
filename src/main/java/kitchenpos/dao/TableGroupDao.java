package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

public interface  TableGroupDao {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}

@Repository
class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    TableGroupRepository(final JdbcTemplateTableGroupDao tableGroupDao,
                         final OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final Long tableGroupId = entity.getId();
        final List<OrderTable> savedOrderTables = entity.getOrderTables();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        entity.setOrderTables(savedOrderTables);
        return tableGroupDao.save(entity);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return Optional.empty();
    }

    @Override
    public List<TableGroup> findAll() {
        return null;
    }
}
