package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTableChangedEmptyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderTableEventListener {

    private final OrderDao orderDao;

    public OrderTableEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void orderTableChangedEmpty(OrderTableChangedEmptyEvent event) {
        validateOrderCompletionByOrderTableId(event.getOrderTableId());
    }

    private void validateOrderCompletionByOrderTableId(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
