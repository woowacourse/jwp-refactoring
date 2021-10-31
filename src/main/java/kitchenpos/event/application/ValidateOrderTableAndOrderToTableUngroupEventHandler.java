package kitchenpos.event.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDao;
import kitchenpos.tablegroup.application.TableGroupStartedToUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTableAndOrderToTableUngroupEventHandler {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    public ValidateOrderTableAndOrderToTableUngroupEventHandler(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void validate(TableGroupStartedToUngroupEvent event) {
        Long tableGroupId = event.getTableGroupId();
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = parseOrderTableIds(orderTables);

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroupTableGroup();
            orderTableDao.save(orderTable);
        }
    }

    private List<Long> parseOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
