package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupVerifier {
    private static final int MIN_TABLE_COUNT = 2;

    private final OrderDao orderDao;

    public TableGroupVerifier(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void verifyOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_TABLE_COUNT) {
            throw new IllegalArgumentException(
                "단체 지정할 주문 테이블은 " + MIN_TABLE_COUNT + "개 이상이어야 합니다.");
        }
    }

    public void verifyNotCompletedOrderStatus(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재하지 않아야 합니다.");
        }
    }
}
