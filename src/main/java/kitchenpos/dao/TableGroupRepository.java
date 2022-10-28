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
        TableGroup tableGroup = tableGroupDao.save(entity);
        List<OrderTable> orderTables = entity.getOrderTables().stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), tableGroup.getId(),
                        orderTable.getNumberOfGuests(), false))
                .map(orderTableDao::save)
                .collect(Collectors.toList());
        return new TableGroup(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroupDao.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        List<TableGroup> tableGroups = tableGroupDao.findAll();

        return tableGroups.stream().map(tableGroup -> new TableGroup(tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTableDao.findAllByTableGroupId(tableGroup.getId()))).collect(Collectors.toList());
    }
}
