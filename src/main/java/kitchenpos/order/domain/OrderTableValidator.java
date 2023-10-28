package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatus(final OrderTable orderTable) {
        if(isOrderUncompleted(orderTable)) {
            throw new IllegalArgumentException("계산이 완료되지 않아 테이블의 상태를 바꿀 수 없습니다.");
        }
    }

    private boolean isOrderUncompleted(final OrderTable orderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        for (Order order : orders) {
            if (!order.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    public void validateUngrouping(final OrderTable orderTable) {
        if (isOrderUncompleted(orderTable)) {
            throw new IllegalArgumentException("계산 완료되지 않은 테이블이 남아있어 단체 지정 해제가 불가능합니다.");
        }
    }
}
