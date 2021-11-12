package kitchenpos.event;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableUngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderTableUngroupEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderTableUngroupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(OrderTable orderTable) {
        OrderTableUngroupEvent orderTableUngroupEvent = new OrderTableUngroupEvent(orderTable);
        applicationEventPublisher.publishEvent(orderTableUngroupEvent);
    }
}
