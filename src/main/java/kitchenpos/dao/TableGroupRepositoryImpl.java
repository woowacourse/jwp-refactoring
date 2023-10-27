package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final TableGroupDao tableGroupDao;

    private final OrderTableRepository orderTableRepository;

    public TableGroupRepositoryImpl(final TableGroupDao tableGroupDao, final OrderTableRepository orderTableRepository) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }

    @Override
    public TableGroup save(TableGroup entity) {
        final Long tableGroupId = tableGroupDao
                .save(new TableGroup(entity.getId(), entity.getCreatedDate(), new OrderTables(List.of())))
                .getId();

        final ArrayList<OrderTable> orderTables = new ArrayList<>();
        for (OrderTable orderTable : entity.getOrderTables()) {
            orderTables.add(orderTableRepository.save(
                    new OrderTable(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false)
            ));
        }

        return new TableGroup(tableGroupId, entity.getCreatedDate(), new OrderTables(orderTables));
    }
}
