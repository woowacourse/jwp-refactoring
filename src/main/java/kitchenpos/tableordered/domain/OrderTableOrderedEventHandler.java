package kitchenpos.tableordered.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderPlacedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableOrderedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableOrderedEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(final OrderPlacedEvent orderPlacedEvent) {
        validateOrderTable(orderPlacedEvent.getOrder());
    }

    public void validateOrderTable(final Order order) {
        validateOrderTable(getOrderTable(order));
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                "빈 상태의 테이블에는 주문을 추가할 수 없습니다.(table id: %d)",
                orderTable.getId()
            ));
        }
    }

    private OrderTable getOrderTable(final Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
    }
}
