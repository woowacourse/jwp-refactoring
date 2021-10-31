package kitchenpos.event.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableStartedToChangeEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderToChangeEmptyOrderTableEventHandler {

    private OrderDao orderDao;

    public ValidateOrderToChangeEmptyOrderTableEventHandler(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @EventListener
    public void ValidateOrderToChangeEmptyOrderTableEventHandler(OrderTableStartedToChangeEmptyEvent event) {
        Long orderTableId = event.getOrderTableId();
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        } }
}
