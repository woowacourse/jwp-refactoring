package kitchenpos.integrationtest.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;

@Sql({"/truncate.sql", "/test_data.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
		}
	}
}
