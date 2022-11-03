package kitchenpos.table.application;

import kitchenpos.order.domain.OrderStatusChangedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusChangedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderStatusChangedEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener(OrderStatusChangedEvent.class)
    public void handle(final OrderStatusChangedEvent event) {
        final OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTableRepository.save(orderTable.changeToCooking(event.getOrderStatus()));
    }
}
