package kitchenpos.config;

import java.time.Clock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
