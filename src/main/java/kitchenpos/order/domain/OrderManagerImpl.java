package kitchenpos.order.domain;

import kitchenpos.common.vo.OrderStatus;
import kitchenpos.exception.NotAllowedUngroupException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderManagerImpl implements OrderManager {

    private OrderRepository orderRepository;

    public OrderManagerImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrdersToUngroup(final List<Long> orderTableIds) {
        final List<OrderStatus> availableOrderStatus = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, availableOrderStatus)) {
            throw new NotAllowedUngroupException("단체 지정을 해제할 수 없는 주문이 존재합니다.");
        }
    }
}
