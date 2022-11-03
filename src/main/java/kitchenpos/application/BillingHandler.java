package kitchenpos.application;

import kitchenpos.domain.order.event.OrderStatusChangedEvent;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BillingHandler {

    private final OrderTableRepository orderTableRepository;

    public BillingHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener(OrderStatusChangedEvent.class)
    public void handle(final OrderStatusChangedEvent event) {
        final OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final TableStatus tableStatus = TableStatus.valueOf(event.getOrderStatus());
        orderTable.changeTableStatus(tableStatus);
    }
}
