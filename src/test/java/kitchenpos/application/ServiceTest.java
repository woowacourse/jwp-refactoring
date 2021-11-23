package kitchenpos.application;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Service.class
        )
})
public class ServiceTest {

}
