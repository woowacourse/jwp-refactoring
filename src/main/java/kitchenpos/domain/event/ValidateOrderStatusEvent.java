package kitchenpos.domain.event;

import kitchenpos.domain.OrderTable;
import org.springframework.context.ApplicationEvent;

public class ValidateOrderStatusEvent extends ApplicationEvent {
    public ValidateOrderStatusEvent(OrderTable source) {
        super(source);
    }
}
