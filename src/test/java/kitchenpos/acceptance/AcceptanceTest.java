package kitchenpos.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT
)
public class AcceptanceTest {

    @LocalServerPort
    protected int port;

    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @BeforeEach
    void reset() {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE orders");
        jdbcTemplate.update("TRUNCATE TABLE order_line_item");
        jdbcTemplate.update("TRUNCATE TABLE menu");
        jdbcTemplate.update("TRUNCATE TABLE menu_group");
        jdbcTemplate.update("TRUNCATE TABLE order_table");
        jdbcTemplate.update("TRUNCATE TABLE table_group");
        jdbcTemplate.update("TRUNCATE TABLE product");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
