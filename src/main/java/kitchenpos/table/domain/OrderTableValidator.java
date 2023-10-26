package kitchenpos.table.domain;

import java.util.NoSuchElementException;
import kitchenpos.common.event.OrderCreationEvent;
import kitchenpos.common.event.OrderTableChangeEmptyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;


    public OrderTableValidator(
            OrderTableRepository orderTableRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    public void validateChangeableEmpty(Long orderTableId) {
        eventPublisher.publishEvent(new OrderTableChangeEmptyEvent(orderTableId));
    }

    @EventListener
    public void validateOrderTable(OrderCreationEvent event) {
        Long orderTableId = event.getOrderTableId();

        if (!orderTableRepository.existsById(orderTableId)) {
            throw new NoSuchElementException("ID에 해당하는 주문 테이블을 찾을 수 없습니다.");
        }
        if (orderTableRepository.existsByIdAndEmptyIsTrue(orderTableId)) {
            throw new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다.");
        }
    }

}
