package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected Response get(String api) {
        return RestAssured.given().log().all()
                .accept("application/json")
                .when().get(api);
    }

    protected Response post(String api, Object body) {
        return RestAssured.given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(api);
    }

    protected Response put(String api, Object body) {
        return RestAssured.given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(api);
    }

    protected Response delete(String api) {
        return RestAssured.given().log().all()
                .when().delete(api);
    }

}
