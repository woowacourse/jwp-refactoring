package kitchenpos.application;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@JdbcTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Repository.class
        ),
        @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Service.class
        )
})
public class ServiceTest {

}
