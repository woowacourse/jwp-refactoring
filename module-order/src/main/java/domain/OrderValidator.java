package domain;

import exception.OrderException;
import org.springframework.stereotype.Component;
import repository.OrderTableRepository;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        if (orderTableRepository.getById(order.getOrderTable().getId()).isEmpty()) {
            throw new OrderException.EmptyTableException();
        }
    }
}
