package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final JdbcTemplateOrderTableDao orderTableDao;

    public TableGroupRepository(final JdbcTemplateTableGroupDao tableGroupDao,
                                final JdbcTemplateOrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final TableGroup tableGroup = tableGroupDao.save(entity);
        final List<OrderTable> orderTables = getOrderTables(entity.getOrderTables(), tableGroup.getId());

        return new TableGroup(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    private List<OrderTable> getOrderTables(final List<OrderTable> orderTables, final Long tableGroupId) {
        return orderTables.stream()
                .map(it -> new OrderTable(it.getId(), tableGroupId, it.getNumberOfGuests(), false))
                .map(orderTableDao::save)
                .collect(Collectors.toList());
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
