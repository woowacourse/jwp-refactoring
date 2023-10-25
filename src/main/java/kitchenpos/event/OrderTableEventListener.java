package kitchenpos.event;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableEventListener {

    private final OrderDao orderDao;

    public OrderTableEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @EventListener
    public void changeEmpty(OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
