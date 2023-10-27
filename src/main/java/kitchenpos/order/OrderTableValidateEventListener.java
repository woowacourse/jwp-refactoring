package kitchenpos.order;

import java.util.Arrays;
import kitchenpos.ordertable.OrderTableEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderTableValidateEventListener {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderTableValidateEventListener(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void validateOrderStatus(final OrderTableEvent event) {
        final Long orderTableId = event.getOrderTableId();

        if (jpaOrderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
