package kitchenpos.event;

import kitchenpos.domain.table.OrderStatusCheckEvent;
import kitchenpos.domain.table.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusCheckEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderStatusCheckEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(OrderTable orderTable) {
        OrderStatusCheckEvent orderStatusCheckEvent = new OrderStatusCheckEvent(orderTable);
        applicationEventPublisher.publishEvent(orderStatusCheckEvent);
    }
}
