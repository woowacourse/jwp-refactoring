package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;

    public TableGroupRepository(TableGroupDao tableGroupDao, OrderTableDao orderTableDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
    }

    public TableGroup save(TableGroup entity) {
        final TableGroup tableGroup = tableGroupDao.save(entity);
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(entity.getOrderTableIds());

        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            final OrderTable savedOrderTable = orderTableDao.save(
                    new OrderTable(orderTable.getId(), tableGroup.getId(), orderTable.getNumberOfGuests(), false));
            savedOrderTables.add(savedOrderTable);
        }
        return new TableGroup(tableGroup.getId(), tableGroup.getCreatedDate(), savedOrderTables);
    }

    public Optional<TableGroup> findById(Long id) {
        return tableGroupDao.findById(id);
    }

    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
