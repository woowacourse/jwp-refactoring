package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.DatabaseCleaner;
import org.assertj.core.api.AbstractIterableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleaner.clear();
    }

    @AfterEach
    public void tearDown() {
        databaseCleaner.clear();
    }

    protected <T> ExtractableResponse<Response> 생성요청(final String url, @Nullable final T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> 조회요청(final String url) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse<Response> 수정요청(final String url, final T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse<Response> 삭제요청(final String url, final T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    protected void 응답일치(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected void 비어있음(final ExtractableResponse<Response> response) {
        assertThat(response.as(List.class)).isEmpty();
    }

    protected void 비어있지않음(final ExtractableResponse<Response> response) {
        assertThat(response.as(List.class)).isNotEmpty();
    }

    protected <T> AbstractIterableAssert 리스트_데이터_검증(final List list, final String fieldName, final T... expected) {
        return assertThat(list)
                .extracting(fieldName)
                .containsExactly(expected);
    }

    protected <T> AbstractIterableAssert 리스트_데이터_검증(final List list, final String fieldName, final List<T> expected) {
        return assertThat(list)
                .extracting(fieldName)
                .isEqualTo(expected);
    }

    protected <T> void 단일_데이터_검증(final T actual, final T expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
