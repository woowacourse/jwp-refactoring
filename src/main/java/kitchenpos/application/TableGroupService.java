package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final List<Long> tableIds) {

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(tableIds);

        if (tableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        OrderTables orderTables = new OrderTables(savedOrderTables);
        TableGroup tableGroup = orderTables.group();

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            List<Order> orders = orderDao.findAllByOrderTableId(orderTable.getId());
            orderTable.setOrders(new Orders(orders));
        }

        OrderTables groupedOrderTables = new OrderTables(orderTables);
        groupedOrderTables.unGroup();

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable);
        }
    }
}
