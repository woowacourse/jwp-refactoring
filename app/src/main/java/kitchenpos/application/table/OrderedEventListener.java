package kitchenpos.application.table;

import static kitchenpos.exception.order.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;

import kitchenpos.domain.order.OrderedEvent;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.order.OrderException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderedEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderedEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void validateTableIsNotEmpty(OrderedEvent orderedEvent) {
        OrderTable orderTable = orderTableRepository.getById(orderedEvent.orderTableId());
        if (orderTable.empty()) {
            throw new OrderException(ORDER_TABLE_CAN_NOT_EMPTY);
        }
    }
}
