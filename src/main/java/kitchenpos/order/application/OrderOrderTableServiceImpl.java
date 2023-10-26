package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.OrderOrderTableService;
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
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("orderTable이 존재하면서 조리중 또는 식사중인 주문 테이블은 empty 상태를 변경 할 수 없습니다.");
        }
    }
}
