package kitchenpos.domain;

import org.springframework.context.ApplicationEventPublisher;

public class CustomEventPublisher {
    public static class AlwaysPass implements ApplicationEventPublisher {
        @Override
        public void publishEvent(Object event) {
        }
    }

    public static class AlwaysFail implements ApplicationEventPublisher {
        @Override
        public void publishEvent(Object event) {
            throw new IllegalArgumentException();
        }
    }
}
