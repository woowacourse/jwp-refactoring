package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableGroupRepository(final TableGroupDao tableGroupDao,
                                final OrderTableDao orderTableDao,
                                final OrderDao orderDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    public TableGroup add(final TableGroup tableGroup) {
        final var savedTableGroup = tableGroupDao.save(tableGroup);
        final var orderTables = tableGroup.getOrderTables()
                .stream()
                .map(orderTableDao::save)
                .collect(Collectors.toList());
        return new TableGroup(
                savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(),
                orderTables
        );
    }

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
                            orderTable.isEmpty(),
                            orders);
                }).collect(Collectors.toList());
    }
}
