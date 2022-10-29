package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.SQLException;
import kitchenpos.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() throws SQLException {
        RestAssured.port = port;
        databaseCleaner.clean();
    }

    protected ExtractableResponse<Response> get(final String uri) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> post(final String uri, final Object body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(uri)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> put(final String uri, final Object body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> delete(final String uri, final Object body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    protected void 상태코드를_검증한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected void 리스트_길이를_검증한다(final ExtractableResponse<Response> response, final String jsonPath, final int size) {
        assertThat(response.jsonPath().getList(jsonPath)).hasSize(size);
    }

    protected void 필드가_Null이_아닌지_검증한다(final ExtractableResponse<Response> response, final String jsonPath) {
        assertThat(response.jsonPath().getLong(jsonPath)).isNotNull();
    }
}
