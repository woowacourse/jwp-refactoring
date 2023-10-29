package kitchenpos.order.domain;

import kitchenpos.common.vo.OrderStatus;
import kitchenpos.exception.InvalidOrderToChangeEmptyException;
import kitchenpos.exception.NotAllowedUngroupException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrdersToUngroup(final List<Long> orderTableIds) {
        final List<OrderStatus> availableOrderStatus = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, availableOrderStatus)) {
            throw new NotAllowedUngroupException("단체 지정을 해제할 수 없는 주문이 존재합니다.");
        }
    }

    @Override
    public void validateOrderStatusIsCompletion(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new InvalidOrderToChangeEmptyException("계산이 완료되지 않아 상태 변경이 불가능합니다.");
        }
    }
}
