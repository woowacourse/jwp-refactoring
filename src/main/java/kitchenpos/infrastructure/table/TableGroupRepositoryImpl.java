package kitchenpos.infrastructure.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderDao;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableDao;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupDao;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableGroupRepositoryImpl(final TableGroupDao tableGroupDao,
                                    final OrderTableDao orderTableDao,
                                    final OrderDao orderDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Override
    public TableGroup add(final TableGroup tableGroup) {
        final var savedTableGroup = tableGroupDao.save(tableGroup);
        final var orderTables = updateAllOrderTables(tableGroup.getOrderTables(), savedTableGroup.getId());

        return new TableGroup(
                savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(),
                orderTables
        );
    }

    private List<OrderTable> updateAllOrderTables(final List<OrderTable> orderTables, final Long tableGroupId) {
        return orderTables.stream()
                .map(orderTable -> {
                    final var entity = new OrderTable(
                            orderTable.getId(),
                            tableGroupId,
                            orderTable.getNumberOfGuests(),
                            false);
                    return orderTableDao.save(entity);
                }).collect(Collectors.toList());
    }

    @Override
    public TableGroup get(final Long id) {
        final var tableGroup = tableGroupDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final var orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        final var fetchedOrderTables = fetchOrders(orderTables);

        return new TableGroup(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                fetchedOrderTables
        );
    }

    private List<OrderTable> fetchOrders(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> {
                    final var orders = orderDao.findByOrderTableId(orderTable.getId());
                    return new OrderTable(
                            orderTable.getId(),
                            orderTable.getTableGroupId(),
                            orderTable.getNumberOfGuests(),
                            orderTable.isEmpty());
                }).collect(Collectors.toList());
    }
}
