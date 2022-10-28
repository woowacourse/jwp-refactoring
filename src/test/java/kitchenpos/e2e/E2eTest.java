package kitchenpos.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.support.DbTableCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * E2E 테스트를 수월하게 작성하기 위한 Support Test 클래스입니다.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class E2eTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DbTableCleaner tableCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        tableCleaner.clear();
    }

    protected ExtractableResponse<Response> GET_요청(final String path) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> POST_요청(final String path, final ProductRequest requestBody) {
        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    protected Executable NOT_NULL_검증(final Product productResponse) {
        return () -> assertThat(productResponse.getId()).isNotNull();
    }

    protected Executable 상태코드_검증(final HttpStatus httpStatus, final ExtractableResponse<Response> response) {
        return () -> assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected Executable 단일_검증(final Object actual, final Object expected) {
        return () -> assertThat(actual).isEqualTo(expected);
    }

    protected Executable 리스트_검증(final List list, final String filedName, final Object... expectedFields) {
        return () -> assertThat(list).extracting(filedName).containsExactlyInAnyOrder(expectedFields);
    }
}
