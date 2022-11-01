package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

public interface TableGroupDao {

    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}

@Repository
class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    TableGroupRepository(final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao,
                         final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(entity);
        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (final OrderTable orderTable : entity.getOrderTables()) {
            orderTable.setTableGroupId(savedTableGroup.getId());
            orderTable.setEmpty(false);
            savedOrderTables.add(jdbcTemplateOrderTableDao.save(orderTable));
        }
        savedTableGroup.setOrderTables(savedOrderTables);
        return savedTableGroup;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        final Optional<TableGroup> tableGroup = jdbcTemplateTableGroupDao.findById(id);
        final OrderTables orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(id);
        tableGroup.ifPresent(it -> it.setOrderTables(orderTables.getOrderTables()));
        return tableGroup;
    }

    @Override
    public List<TableGroup> findAll() {
        final List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();
        for (final TableGroup tableGroup : tableGroups) {
            final OrderTables orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId());
            tableGroup.setOrderTables(orderTables.getOrderTables());
        }
        return tableGroups;
    }
}
