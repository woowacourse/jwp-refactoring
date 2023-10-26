package kitchenpos.ordertable.eventlistener;

import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class TableGroupEventListener {

    private final OrderTableDao orderTableDao;

    public TableGroupEventListener(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void createTableGroup(TableGroupCreatedEvent event) {
        validateOrderTables(event);
        saveOrderTables(event);
    }

    private void validateOrderTables(TableGroupCreatedEvent event) {
        List<Long> orderTableIds = event.getOrderTableIds();
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void saveOrderTables(TableGroupCreatedEvent event) {
        final Long tableGroupId = event.getId();
        List<Long> orderTableIds = event.getOrderTableIds();
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        for (final OrderTable OrderTable : orderTables) {
            OrderTable.group(tableGroupId);
            orderTableDao.save(OrderTable);
        }
    }
}
