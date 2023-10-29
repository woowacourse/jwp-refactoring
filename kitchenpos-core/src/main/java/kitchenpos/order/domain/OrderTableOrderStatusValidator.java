package kitchenpos.order.domain;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderStatusValidator;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeEmptyException;
import org.springframework.stereotype.Component;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Component
public class OrderTableOrderStatusValidator implements OrderStatusValidator {
    
    private final OrderRepository orderRepository;
    
    public OrderTableOrderStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    @Override
    public void validateOrderStatusNotCompleted(Long tableId) {
        boolean isOrderInProgress = orderRepository.existsByOrderTableIdAndOrderStatusIn(tableId, List.of(COOKING, MEAL));
        if (isOrderInProgress) {
            throw new InvalidOrderTableChangeEmptyException("테이블에 속하는 주문의 상태가 COOKING 또는 MEAL이라면 테이블의 상태를 변경할 수 없습니다");
        }
    }
    
}
