package kitchenpos.application;

import java.util.Arrays;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.event.OrderStatusChangedEvent;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BillingHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public BillingHandler(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @EventListener(OrderStatusChangedEvent.class)
    public void handle(final OrderStatusChangedEvent event) {
        final OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (!orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            final TableStatus tableStatus = TableStatus.find(event.getOrderStatus());
            orderTable.changeTableStatus(tableStatus);
        }
    }
}
