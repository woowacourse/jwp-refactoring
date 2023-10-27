package kitchenpos.order;

import kitchenpos.order.application.OrderRepository;
import kitchenpos.ordertable.application.OrderTableValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final Long orderTableId, final List<String> orderStatuses) {
        final List<OrderStatus> orderStatusResult = orderStatuses.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatusResult)) {
            throw new IllegalArgumentException("해당 테이블 그룹에 주문을 진행할 수 없습니다.");
        }
    }
}
