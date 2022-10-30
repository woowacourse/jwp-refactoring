package kitchenpos.e2e;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.support.DbTableCleaner;
import kitchenpos.support.ProductFixture.WrapProductResponse;
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
    protected ExtractableResponse<Response> GET_요청(String path) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> POST_요청(String path, Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> PUT_요청(String path, Long id, Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path, id)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> DELETE_요청(String path, Long id, Object requestBody) {

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

    protected Executable HTTP_STATUS_검증(HttpStatus httpStatus, ExtractableResponse<Response> response) {

        return () -> assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected <T> Executable NOT_NULL_검증(T actual) {

        return () -> assertThat(actual).isNotNull();
    }

    protected <T> Executable 단일_검증(T actual, T expected) {

        return () -> assertThat(actual).isEqualTo(expected);
    }

    protected <T> Executable 단일_검증(T actual, String fieldName, Object... expectedFields) {

        return 리스트_검증(List.of(actual), fieldName, expectedFields);
    }

    protected Executable 리스트_검증(List list, String filedName, Object... expectedFields) {

        return () -> assertThat(list).extracting(filedName).containsExactlyInAnyOrder(expectedFields);
    }

    /**
     * 시간에 대한 근사치 검증
     * <p/>
     * ex) 검증하고자 하는 시각이 현 시간 부로 3분 이내의 시각과 같음
     */
    protected Executable 시간_근사_검증(LocalDateTime actualDateTime, int coefficient, ChronoUnit unit) {
        return () -> assertThat(actualDateTime).isCloseTo(now(), within(coefficient, unit));
    }

    protected List<WrapProductResponse> extractListExactly(ExtractableResponse<Response> 응답) {
        return 응답.body().as(new TypeRef<>() {
        });
    }

    /**
     * 응답값으로 scale: 2, precision: 7인 BigDecimal이 넘어오는데 이것을 정규화합니다.
     */
    private void normalizePrices(List<WrapProductResponse> responses) {
        for (WrapProductResponse response : responses) {
            response.normalizePrice();
        }
    }

    protected List<WrapProductResponse> extractListExactlyAndNormalizePrices(ExtractableResponse<Response> 응답) {

        final List<WrapProductResponse> responses = extractListExactly(응답);

        normalizePrices(responses);

        return responses;
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
    protected List<Executable> 리스트_검증(List actualList, AssertionPair... pairs) {

        ListAssert listAssert = assertThat(actualList);

        return stream(pairs)
                .map(pair -> pair.toExecutable(listAssert))
                .collect(Collectors.toList());
    }

    /**
     * 슈퍼타입 토큰을 이용해서 한 단계 이상 깊이의 제네릭의 값을 추출합니다.
     */
    protected <T> List<T> extractHttpBody(ExtractableResponse<Response> 응답) {
        return 응답.body().as(new TypeRef<>() {
        });
    }

    protected static class AssertionPair {

        private String fieldName;
        private Object[] expected;

        private AssertionPair(String fieldName, Object... expected) {

            this.fieldName = fieldName;
            this.expected = expected;
        }

        public static AssertionPair row(String key, Object... expected) {

            return new AssertionPair(key, expected);
        }

        public Executable toExecutable(ListAssert listAssert) {

            return () -> listAssert.extracting(fieldName).containsExactlyInAnyOrder(expected);
        }
    }

    protected static class BigDecimalAssertionPair extends AssertionPair {

        private BigDecimalAssertionPair(String fieldName, Object... expected) {
            super(fieldName, expected);
        }

        /**
         * BigDecimal 값을 검증하기 위함
         */
        public static BigDecimalAssertionPair rowBigDecimal(String key, int scale, BigDecimal... expected) {

            final BigDecimal[] convert = convertBigDecimalToIntegers(scale, expected);

            return new BigDecimalAssertionPair(key, convert);
        }

        public static BigDecimalAssertionPair rowBigDecimal(String key, BigDecimal... expected) {

            return rowBigDecimal(key, 2, expected);
        }

        public Executable toExecutable(ListAssert listAssert) {

            return () -> listAssert.extracting(super.fieldName).containsExactlyInAnyOrder(super.expected);
        }

        private static BigDecimal[] convertBigDecimalToIntegers(int scale, BigDecimal... expected) {

            return stream(expected)
                    .map(it -> it.setScale(2))
                    .toArray(BigDecimal[]::new);
        }
    }
}
