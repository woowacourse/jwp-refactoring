package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.exception.InvalidOrderTableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrderLineItemNotEmpty(order.getOrderLineItems());
        validateOrderTableNotEmpty(findOrderTableById(order.getOrderTableId()));
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(InvalidOrderTableException::new);
    }

    private void validateOrderLineItemNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderException();
        }
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException();
        }
    }
}
