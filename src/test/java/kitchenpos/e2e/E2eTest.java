package kitchenpos.e2e;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.support.DbTableCleaner;
import org.assertj.core.api.ListAssert;
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

        tableCleaner.clearAll();
    }

    /**
     * ---------------
     * REST 요청 목록
     * ---------------
     */
    protected ExtractableResponse<Response> GET_요청(final String path) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> POST_요청(final String path, final Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> PUT_요청(final String path, final Long id, final Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path, id)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> DELETE_요청(final String path, final Long id, final Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path, id)
                .then().log().all()
                .extract();
    }

    /**
     * -------------------
     * 검증 편의 메서드 목록
     * -------------------
     */

    protected Executable HTTP_STATUS_검증(final HttpStatus httpStatus, final ExtractableResponse<Response> response) {

        return () -> assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected <T> Executable NOT_NULL_검증(final T actual) {

        return () -> assertThat(actual).isNotNull();
    }

    protected <T> Executable 단일_검증(final T actual, final T expected) {

        return () -> assertThat(actual).isEqualTo(expected);
    }

    protected <T> Executable 단일_검증(final T actual, final String fieldName, final Object... expectedFields) {

        return 리스트_검증(List.of(actual), fieldName, expectedFields);
    }

    protected Executable 리스트_검증(final List list, final String filedName, final Object... expectedFields) {

        return () -> assertThat(list).extracting(filedName).containsExactlyInAnyOrder(expectedFields);
    }

    /**
     * 시간에 대한 근사치 검증
     * <p/>
     * ex) 검증하고자 하는 시각이 현 시간 부로 3분 이내의 시각과 같음
     */
    protected Executable 시간_근사_검증(final LocalDateTime actualDateTime, final int coefficient, final ChronoUnit unit) {
        return () -> assertThat(actualDateTime).isCloseTo(now(), within(coefficient, unit));
    }


    /**
     *
     * assertAll 안에 사용하기 위한 구문
     * <p/>
     * 사용예시)
     * <pre>
     * 리스트_검증(상품_리스트,
     *         row("id", 1, 2),
     *         row("name", 단짜_두_마리_메뉴.getName(), 간장_양념_세_마리_메뉴.getName())
     * )
     * </pre>
     * <p/>
     * 아래와 같은 문장들을 만들어 줍니다
     * <pre>
     * () -> listAssert.extracting("필드명").containsExactlyInAnyOrder(값_1, 값_2),
     * () -> listAssert.extracting("필드명2").containsExactlyInAnyOrder(값_4),
     * ...
     * </pre>
     * 뎁스가 한 단계 에서만 허용이 됩니다.
     * <p/>
     * 예를 들어 menu.menuProducts[0].name 등의 사용은 불가
     *<p/>
     * 추후 B/DFS로 검증 로직을 수정할 수 있음
     */
    protected List<Executable> 리스트_검증(final List actualList, final AssertionPair... pairs) {

        final ListAssert listAssert = assertThat(actualList);

        return stream(pairs)
                .map(pair -> pair.toExecutable(listAssert))
                .collect(Collectors.toList());
    }

    /**
     * 슈퍼타입 토큰을 이용해서 한 단계 이상 깊이의 제네릭의 값을 추출합니다.
     */
    protected  <T> List<T> extractHttpBody(final ExtractableResponse<Response> 응답) {
        return 응답.body().as(new TypeRef<List<T>>() {
        });
    }

    protected static class AssertionPair {

        private String fieldName;
        private Object[] expected;

        private AssertionPair(final String fieldName, final Object... expected) {

            this.fieldName = fieldName;
            this.expected = expected;
        }

        public static AssertionPair row(final String key, final Object... expected) {

            return new AssertionPair(key, expected);
        }

        public Executable toExecutable(final ListAssert listAssert) {

            return () -> listAssert.extracting(fieldName).containsExactlyInAnyOrder(expected);
        }
    }
}
