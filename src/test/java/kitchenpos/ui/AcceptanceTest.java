package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> makeResponse(String url, TestMethod testMethod) {
        return makeResponse(url, testMethod, null);
    }

    protected ExtractableResponse<Response> makeResponse(String url, TestMethod testMethod,
        Object requestBody) {
        RequestSpecification request = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (Objects.nonNull(requestBody)) {
            request = request.body(requestBody);
        }
        return testMethod.extractedResponse(request, url);
    }
}
