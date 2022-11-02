package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.dao.OrderDao;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupValidator {

    private final OrderDao orderDao;

    public TableUngroupValidator(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void validateUngroup(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 테이블이 존재합니다.");
        }
    }
}
