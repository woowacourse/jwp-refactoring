package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.repository.dao.JdbcTemplateOrderTableDao;
import kitchenpos.order.repository.dao.JdbcTemplateTableGroupDao;
import kitchenpos.order.repository.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    public TableGroupRepository(final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao,
                                final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final TableGroup tableGroup = jdbcTemplateTableGroupDao.save(entity);
        final List<OrderTable> orderTables = entity.getOrderTables()
                .stream()
                .map(orderTable -> new OrderTable(
                        orderTable.getId(),
                        tableGroup.getId(),
                        orderTable.getNumberOfGuests(),
                        false
                ))
                .map(jdbcTemplateOrderTableDao::save)
                .collect(Collectors.toList());
        return TableGroup.of(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return jdbcTemplateTableGroupDao.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        final List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();
        return tableGroups.stream()
                .map(tableGroup -> TableGroup.of(
                        tableGroup.getId(),
                        tableGroup.getCreatedDate(),
                        jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId()))
                ).collect(Collectors.toList());
    }
}
