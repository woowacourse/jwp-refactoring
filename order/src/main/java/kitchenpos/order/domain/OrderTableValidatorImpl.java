package kitchenpos.order.domain;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableValidator;
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
    public void validateOrderStatus(final Long orderTableId, final List<String> orderStatus) {
        final List<OrderStatus> excludeCompletionOrderStatus = orderStatus.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, excludeCompletionOrderStatus)) {
            throw new IllegalArgumentException();
        }
    }
}
