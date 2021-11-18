package kitchenpos.event;

import kitchenpos.table.domain.OrderStatusCheckEvent;
import kitchenpos.table.domain.OrderTable;
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
