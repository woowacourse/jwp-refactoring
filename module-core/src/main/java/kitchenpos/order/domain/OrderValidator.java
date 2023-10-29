package kitchenpos.order.domain;

import java.util.List;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertablegroup.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusByOrderTableIds(final List<Long> orderTableIds) {
        orderRepository.findAllByOrderTableIdIn(orderTableIds)
                .forEach(Order::validateUncompleted);
    }

    @Override
    public void validateOrderStatusByOrderTableId(final Long orderTableId) {
        orderRepository.getById(orderTableId).validateUncompleted();
    }

}
