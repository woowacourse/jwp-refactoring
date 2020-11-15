package kitchenpos.domain.event;

import kitchenpos.domain.OrderTable;
import org.springframework.context.ApplicationEvent;

public class ValidateTableStatusEvent extends ApplicationEvent {
    public ValidateTableStatusEvent(OrderTable source) {
        super(source);
    }
}
