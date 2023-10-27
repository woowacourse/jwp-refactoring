package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.ValidateOrderIsNotCompletionDto;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    public OrderEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    private void validateOrderIsNotCompletion(final ValidateOrderIsNotCompletionDto dto) {
        final List<Order> orders = orderRepository.findAllByOrderTableIdIn(dto.getOrderTableIds());
        orders.forEach(this::validateCompletion);
    }

    private void validateCompletion(final Order order) {
        if (order.isNotCompletion()) {
            throw new IllegalArgumentException("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}
