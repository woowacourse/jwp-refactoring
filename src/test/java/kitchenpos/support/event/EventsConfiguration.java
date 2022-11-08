package kitchenpos.support.event;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class EventsConfiguration {

    @Bean
    public Events applicationEvents() {
        return new Events();
    }
}
