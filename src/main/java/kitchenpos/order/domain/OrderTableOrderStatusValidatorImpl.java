package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableOrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableOrderStatusValidatorImpl implements OrderTableOrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderTableOrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(final Long orderTableId) {
        final boolean isOrderStillInProgress = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                                                                    List.of(OrderStatus.COOKING, OrderStatus.MEAL));
        if (isOrderStillInProgress) {
            throw new IllegalArgumentException("진행중인 주문이 있어, 주문 테이블의 상태를 변경할 수 없습니다.");
        }
    }
}
