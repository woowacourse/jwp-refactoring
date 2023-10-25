package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class OrderOrderTableServiceImpl implements OrderOrderTableService {

    private final OrderRepository orderRepository;

    public OrderOrderTableServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderTableIdAndOrderStatusIn(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("orderTable이 존재하면서 조리중 또는 식사중인 주문 테이블은 empty 상태를 변경 할 수 없습니다.");
        }
    }
}
