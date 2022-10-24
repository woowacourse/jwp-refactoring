package acceptance;

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
        classes = Application.class
)
public abstract class AcceptanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE order_table");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("DELETE FROM product WHERE id > 6");
        jdbcTemplate.execute("DELETE FROM menu_group WHERE id > 4");
        jdbcTemplate.execute("DELETE FROM menu WHERE id > 6");
        jdbcTemplate.execute("DELETE FROM menu_product WHERE seq > 6");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");

        jdbcTemplate.execute("ALTER TABLE order_table ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (3, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (4, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (5, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (6, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (7, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (8, 0, true)");
    }
}
