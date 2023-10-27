package kitchenpos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/test-truncate.sql"})
public abstract class ServiceTest {

    @Autowired
    protected TestFixtureBuilder testFixtureBuilder;
}
