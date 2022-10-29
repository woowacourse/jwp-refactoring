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

    protected void 상태코드를_검증한다(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected void 리스트_길이를_검증한다(final ExtractableResponse<Response> response, final String jsonPath, final int size) {
        assertThat(response.jsonPath().getList(jsonPath)).hasSize(size);
    }

    protected void 필드가_Null이_아닌지_검증한다(final ExtractableResponse<Response> response, final String jsonPath) {
        assertThat(response.jsonPath().getLong(jsonPath)).isNotNull();
    }

    protected void 숫자_필드값을_검증한다(final ExtractableResponse<Response> response, final String jsonPath,
                                final long expected) {
        assertThat(response.jsonPath().getLong(jsonPath)).isEqualTo(expected);
    }

    protected void Boolean_필드값을_검증한다(final ExtractableResponse<Response> response, final String jsonPath,
                                     final boolean expected) {
        assertThat(response.jsonPath().getBoolean(jsonPath)).isEqualTo(expected);
    }
}
