package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

public interface TableGroupDao {
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
        final TableGroup saveTableGroup = tableGroupDao.save(entity);
        for (final OrderTable savedOrderTable : entity.getOrderTables()) {
            savedOrderTable.setTableGroupId(saveTableGroup.getId());
            orderTableDao.save(savedOrderTable);
        }
        return saveTableGroup;
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
