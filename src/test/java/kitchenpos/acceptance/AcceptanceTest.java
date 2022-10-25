package kitchenpos.acceptance;

import io.restassured.RestAssured;
import kitchenpos.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {Application.class}
)
abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("DELETE FROM menu_group WHERE id >= 5");
        jdbcTemplate.update("DELETE FROM product WHERE id >= 7");
        jdbcTemplate.update("DELETE FROM menu WHERE id >= 7");
        jdbcTemplate.update("DELETE FROM menu_product WHERE menu_id >= 7");
        jdbcTemplate.update("DELETE FROM order_table WHERE id >= 9");
        jdbcTemplate.update("UPDATE order_table SET empty = true");
        jdbcTemplate.update("TRUNCATE TABLE orders");
        jdbcTemplate.update("TRUNCATE TABLE order_line_item");
        jdbcTemplate.update("TRUNCATE TABLE table_group");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
