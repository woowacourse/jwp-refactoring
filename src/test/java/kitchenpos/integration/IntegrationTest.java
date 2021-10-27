package kitchenpos.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "1000000")
public class IntegrationTest {
    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected FixtureMaker fixtureMaker;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("TRUNCATE TABLE order_line_item");
        jdbcTemplate.execute("TRUNCATE TABLE menu");
        jdbcTemplate.execute("TRUNCATE TABLE menu_group");
        jdbcTemplate.execute("TRUNCATE TABLE menu_product");
        jdbcTemplate.execute("TRUNCATE TABLE order_table");
        jdbcTemplate.execute("TRUNCATE TABLE table_group");
    }
}
