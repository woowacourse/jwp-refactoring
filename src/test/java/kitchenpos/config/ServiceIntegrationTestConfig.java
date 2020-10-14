package kitchenpos.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(excludeFilters = {@ComponentScan.Filter(
    type = FilterType.ANNOTATION,
    classes = Controller.class
)})
public class ServiceIntegrationTestConfig {
    // TODO: 2020/10/17 컨트롤러 빈을 제외하도록 설정했으나 적용되지 않음.
}
