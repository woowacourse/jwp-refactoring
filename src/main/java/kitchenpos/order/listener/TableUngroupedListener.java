package kitchenpos.order.listener;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupedListener {

    private final OrderRepository orderRepository;

    public TableUngroupedListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void ungroupTable(final TableUngroupedEvent event) {
        final List<Long> orderTableIds = event.getOrderTableIds();

        final boolean isProceeding = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        if (isProceeding) {
            throw new IllegalArgumentException();
        }
    }
}
