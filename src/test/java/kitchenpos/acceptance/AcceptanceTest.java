package kitchenpos.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/clear-database.sql")
public abstract class AcceptanceTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
