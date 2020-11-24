package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableVerifier {
    private OrderRepository orderRepository;

    public OrderTableVerifier(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void verifyNotCompletedOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재하지 않아야 합니다.");
        }
    }
}
