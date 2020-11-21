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

    public void verifyNotCompleted(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
