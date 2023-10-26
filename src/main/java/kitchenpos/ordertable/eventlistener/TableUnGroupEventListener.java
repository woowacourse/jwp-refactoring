package kitchenpos.ordertable.eventlistener;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableUnGroupEventListener {

    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableUnGroupEventListener(TableGroupDao tableGroupDao, OrderTableDao orderTableDao, OrderDao orderDao) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @EventListener
    public void ungroup(TableGroupUngroupEvent event) {
        TableGroup tableGroup = tableGroupDao.findById(event.getId())
                .orElseThrow(IllegalArgumentException::new);

        final List<OrderTable> orderTables = findOrderTables(tableGroup);
        saveOrderTables(orderTables);
    }

    private List<OrderTable> findOrderTables(TableGroup tableGroup) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    private void saveOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(null);
            orderTableDao.save(orderTable);
        }
    }
}
