package kitchenpos.event;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TableGroupEventListener {

    private final OrderTableDao orderTableDao;

    public TableGroupEventListener(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void createTableGroup(TableGroup tableGroup) {
        validateOrderTables(tableGroup);
        saveOrderTables(tableGroup);
    }

    private void validateOrderTables(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

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

    private void saveOrderTables(TableGroup tableGroup) {
        final Long tableGroupId = tableGroup.getId();
        for (final OrderTable OrderTable : tableGroup.getOrderTables()) {
            OrderTable.group(tableGroupId);
            orderTableDao.save(OrderTable);
        }
    }
}
