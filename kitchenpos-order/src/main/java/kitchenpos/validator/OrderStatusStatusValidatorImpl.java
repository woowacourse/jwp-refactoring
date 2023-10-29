package kitchenpos.validator;

import java.util.List;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusStatusValidatorImpl implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusStatusValidatorImpl(final OrderRepository orderRepository) {
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
