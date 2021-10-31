package kitchenpos.event.application;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDao;
import kitchenpos.tablegroup.application.TableGroupStartedToCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTableToCreateTableGroupEventHandler {
    private OrderTableDao orderTableDao;

    public ValidateOrderTableToCreateTableGroupEventHandler(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void validateOrderTableSizeAndEmpty(TableGroupStartedToCreateEvent event) {
        List<OrderTable> orderTables = event.getOrderTables();
        List<Long> orderTableIds = event.getOrderTableIds();
        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
