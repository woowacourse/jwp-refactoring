package kitchenpos.domain.event;

import kitchenpos.domain.Menu;
import org.springframework.context.ApplicationEvent;

public class ValidateMenuPriceEvent extends ApplicationEvent {
    public ValidateMenuPriceEvent(Menu source) {
        super(source);
    }
}
