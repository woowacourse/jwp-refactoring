package kitchenpos.order.service;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateExistInOrderTable(final Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블은 주문을 할 수 없습니다.");
        }
    }
}
