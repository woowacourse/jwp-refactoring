package kitchenpos.table.application;

import static kitchenpos.table.exception.OrderTableExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;

import kitchenpos.event.OrderedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.OrderTableException;
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
            throw new OrderTableException(ORDER_TABLE_CAN_NOT_EMPTY);
        }
    }
}
