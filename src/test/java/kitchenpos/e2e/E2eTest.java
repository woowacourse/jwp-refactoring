package kitchenpos.e2e;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.MenuFixture.createMenuRequest;
import static kitchenpos.support.MenuGroupFixture.단짜_두_마리_메뉴;
import static kitchenpos.support.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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


    public static final String PRODUCT_URL = "/api/products";

    public static final String MENU_URL = "/api/menus";

    public static final String MENU_GROUP_URL = "/api/menu-groups";
    public static final String ORDER_TABLE_URL = "/api/tables";
    public static final String ORDER_URL = "/api/orders";

    @LocalServerPort
    private int port;

    @Autowired
    private DbTableCleaner tableCleaner;

    @BeforeEach
    void setUp() {

        RestAssured.port = port;

        tableCleaner.clear();
    }

    /**
     * REST 요청 목록
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

    protected ExtractableResponse<Response> PUT_요청(final String path, final Object requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> PUT_요청(final String pathWithId, final Long id, final Object requestBody) {
        final String path = String.format(pathWithId, id);

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    /**
     * 검증 편의 메서드 목록
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
     *
     * assertThat 안에 사용하기 위한 구문
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

    /**
     * 생성 (API) 요청 편의 메서드 목록
     */

    protected Order 주문_생성(final OrderStatus orderStatus) {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Long 주문테이블_ID = POST_요청(ORDER_TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        final LocalDateTime 주문일시 = now().minusMinutes(1);

        final Order 주문 = new Order(주문테이블_ID, orderStatus.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(Order.class);
    }

    protected Order 주문_생성(final Long 주문테이블_ID, final LocalDateTime 주문일시) {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Order 주문 = new Order(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(Order.class);
    }

    protected Order 주문_생성() {

        final Long 메뉴_ID = 메뉴_생성_및_ID_반환();

        final Long 주문테이블_ID = POST_요청(ORDER_TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();

        final LocalDateTime 주문일시 = now().minusMinutes(1);

        final Order 주문 = new Order(주문테이블_ID, MEAL.name(), 주문일시, List.of(new OrderLineItem(메뉴_ID, 1)));

        return POST_요청(ORDER_URL, 주문).as(Order.class);
    }

    protected Long 메뉴_생성_및_ID_반환() {

        final Long 메뉴그룹_ID = 메뉴그룹_생성_및_ID_반환();

        final Long 후라이드치킨_ID = 상품_생성_및_ID_반환();

        final List<MenuProduct> 메뉴상품_리스트 = List.of(new MenuProduct(후라이드치킨_ID, 1));


        return POST_요청(MENU_URL, createMenuRequest("단 치킨 한 마리", 15_000, 메뉴그룹_ID, 메뉴상품_리스트))
                .as(Menu.class)
                .getId();
    }

    protected Long 메뉴_생성_및_ID_반환(final Long 메뉴그룹_ID, final List<MenuProduct> 메뉴상품_리스트) {
        return POST_요청(MENU_URL, createMenuRequest("단 치킨 한 마리", 15_000, 메뉴그룹_ID, 메뉴상품_리스트))
                .as(Menu.class)
                .getId();
    }

    protected Long 상품_생성_및_ID_반환() {
        return POST_요청(PRODUCT_URL, 후라이드_치킨)
                .as(Product.class)
                .getId();
    }

    protected Long 메뉴그룹_생성_및_ID_반환() {
        return POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴)
                .as(MenuGroup.class)
                .getId();
    }

}
