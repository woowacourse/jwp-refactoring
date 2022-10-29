package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupRepository(final JdbcTemplateTableGroupDao tableGroupDao,
                                final JdbcTemplateOrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    public TableGroup save(final TableGroup entity) {
        final TableGroup tableGroup = tableGroupDao.save(entity);

        final Long tableGroupId = tableGroup.getId();

        final List<OrderTable> orderTables = entity.getOrderTables();
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.setTableGroupId(tableGroupId);
            orderTableDao.save(orderTable);
        }
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    public Optional<TableGroup> findById(final Long id) {
        return tableGroupDao.findById(id);
    }

    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
