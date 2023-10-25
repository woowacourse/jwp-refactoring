package kitchenpos.order.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Component
public class OrderTableEventListener {

    private final OrderRepository orderRepository;

    public OrderTableEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void validateOrderStatus(final OrderTableChangeEmptyEvent event) {
        final Long orderTableId = event.getOrderTableId();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문 테이블이 조리 중이거나 식사 중입니다.");
        }
    }
}
