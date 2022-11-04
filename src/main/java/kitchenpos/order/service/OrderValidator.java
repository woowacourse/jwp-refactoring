package kitchenpos.order.service;

import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateExistInOrderTable(final Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
