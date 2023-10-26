package kitchenpos.order.domain;

import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableChangeEmptyOrderValidator implements OrderTableChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ValidResult validate(Long orderTableId) {
        Order order = findOrderByOrderTableId(orderTableId);
        if (!order.isCompletion()) {
            return ValidResult.failure("계산 완료 상태가 아닌 주문이 있는 테이블은 상태를 변경할 수 없습니다. orderTableId=" + orderTableId);
        }
        return ValidResult.success();
    }

    private Order findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId)
            .orElseThrow(() -> new KitchenPosException("해당 주문이 없습니다. orderTableId=" + orderTableId));
    }
}
