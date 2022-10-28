package kitchenpos.dao;

import java.time.LocalDateTime;
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

    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    TableGroupRepository(final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao,
                         final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        entity.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(entity);
        final Long tableGroupId = savedTableGroup.getId();
        final List<OrderTable> orderTables = entity.getOrderTables();
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setEmpty(false);
            jdbcTemplateOrderTableDao.save(orderTable);
        }
        savedTableGroup.setOrderTables(orderTables);
        return savedTableGroup;
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
