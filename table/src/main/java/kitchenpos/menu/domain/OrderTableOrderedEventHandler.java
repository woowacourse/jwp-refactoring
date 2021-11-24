package kitchenpos.menu.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class OrderTableOrderedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableOrderedEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    public void handle(final OrderTableOrderedEvent orderTableOrderedEvent) {
        validateOrderTable(orderTableOrderedEvent);
    }

    public void validateOrderTable(final OrderTableOrderedEvent orderTableOrderedEvent) {
        validateOrderTable(getOrderTable(orderTableOrderedEvent));
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                "빈 상태의 테이블에는 주문을 추가할 수 없습니다.(table id: %d)",
                orderTable.getId()
            ));
        }
    }

    private OrderTable getOrderTable(final OrderTableOrderedEvent orderTableOrderedEvent) {
        return orderTableRepository.findById(orderTableOrderedEvent.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
    }
}
