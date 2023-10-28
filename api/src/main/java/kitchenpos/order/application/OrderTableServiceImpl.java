package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableServiceImpl implements OrderTableService {

    private final OrderRepository orderRepository;

    public OrderTableServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public void checkOrderStatusInCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public void checkOrdersStatusInCookingOrMeal(List<Long> orderTableIds) {
        List<OrderStatus> invalidOrderStatusToUngroup = List.of(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds,
                invalidOrderStatusToUngroup
        )) {
            throw new IllegalArgumentException();
        }
    }


}
