package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableGroupDao orderTableGroupDao;

    public OrderTableGroupService(OrderDao orderDao, OrderTableDao orderTableDao,
                                  OrderTableGroupDao orderTableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableGroupDao = orderTableGroupDao;
    }

    @Transactional
    public OrderTableGroup create(List<OrderTable> orderTables) {
        validateGroupingOrderTable(orderTables);
        return orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(), orderTables));
    }

    private void validateGroupingOrderTable(List<OrderTable> orderTables) {
        if (orderTables.stream()
                .anyMatch(OrderTable::isTableGrouping)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateUpgroupingOrderStatus(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.upgroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateUpgroupingOrderStatus(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
