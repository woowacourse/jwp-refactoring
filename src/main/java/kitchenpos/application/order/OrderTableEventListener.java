package kitchenpos.application.order;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTableChangedEmptyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderTableEventListener {

    private final OrderDao orderDao;

    public OrderTableEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
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
