package kitchenpos.core.event;

import kitchenpos.domain.order.event.OrderStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;

public class Events {

    private static ApplicationEventPublisher publisher;

    static void setPublisher(final ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void publishEvent(final OrderStatusChangedEvent event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}