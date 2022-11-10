package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderDao orderDao;

    public TableValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validateTable(final Long orderTableId) {
        validateTableOrderStatus(orderTableId);
    }

    public void validateTableGroup(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 Cooking이거나 Meal인 경우 그룹 해제할 수 없습니다.");
        }
    }

    private void validateTableOrderStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 Cooking이나 Meal일 경우 테이블의 상태를 변경할 수 없습니다.");
        }
    }
}
