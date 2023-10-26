package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableUngroupValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableUngroupOrderValidator implements OrderTableUngroupValidator {

    private final OrderRepository orderRepository;

    public OrderTableUngroupOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ValidResult validate(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        for (Order order : orders) {
            if (!order.isCompletion()) {
                return ValidResult.failure("계산 완료 상태가 아닌 주문이 있는 테이블은 그룹을 해제할 수 없습니다.");
            }
        }
        return ValidResult.success();
    }
}
