package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableGroupService {

    private final OrderDao orderDao;
    private final OrderTableGroupDao orderTableGroupDao;

    public OrderTableGroupService(OrderDao orderDao, OrderTableGroupDao orderTableGroupDao) {
        this.orderDao = orderDao;
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
        OrderTableGroup orderTableGroup = orderTableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        validateUngroupingOrderStatus(orderTableGroup.getOrderTables());
        orderTableGroup.ungroup();
    }

    private void validateUngroupingOrderStatus(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
