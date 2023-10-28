package kitchenpos.validator;

import java.util.List;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements OrderValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public void validateById(final Long id) {
        orderRepository.findByOrderTableId(id)
                .ifPresent(order -> order.validateIsNotComplete());
    }

    @Override
    public void validateByIdIn(final List<Long> ids) {
        orderRepository.findAllByOrderTableIdIn(ids).stream()
                .forEach(order -> order.validateIsNotComplete());
    }
}
