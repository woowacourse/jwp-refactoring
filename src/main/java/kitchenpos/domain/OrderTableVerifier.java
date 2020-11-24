package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import org.springframework.stereotype.Component;

@Component
public class OrderTableVerifier {
    private OrderDao orderDao;

    public OrderTableVerifier(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void verifyNotCompletedOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재하지 않아야 합니다.");
        }
    }
}
