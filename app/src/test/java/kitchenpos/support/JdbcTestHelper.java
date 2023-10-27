package kitchenpos.support;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = @ComponentScan.Filter(Repository.class))
public abstract class JdbcTestHelper {

}
