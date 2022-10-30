package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    protected Product 토마토파스타;
    protected Product 목살스테이크;
    protected Product 탄산음료;

    protected MenuGroup 파스타;
    protected MenuGroup 스테이크;
    protected MenuGroup 음료;
    protected MenuGroup 세트;

    protected Menu 파스타한상;

    protected OrderTable 빈_테이블1;
    protected OrderTable 빈_테이블2;

    protected OrderTable 손님있는_식사중_테이블;
    protected OrderTable 손님있는_테이블;

    protected Order 식사중인_주문;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        토마토파스타 = 상품_등록("토마토파스타", 15000L);
        목살스테이크 = 상품_등록("목살스테이크", 20000L);
        탄산음료 = 상품_등록("탄산음료", 4000L);
        파스타 = 메뉴_그룹_등록("파스타");
        스테이크 = 메뉴_그룹_등록("스테이크");
        음료 = 메뉴_그룹_등록("음료");
        세트 = 메뉴_그룹_등록("세트");
        파스타한상 = 메뉴_등록("파스타한상", 35000L, 세트, 토마토파스타.getId(), 목살스테이크.getId(), 탄산음료.getId());
        빈_테이블1 = 테이블_등록(0, true);
        빈_테이블2 = 테이블_등록(0, true);
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        식사중인_주문 = 메뉴_주문(손님있는_식사중_테이블, 파스타한상.getId());
        식사중인_주문 = 주문을_식사_상태로_만든다(식사중인_주문);
    }

    protected Product 상품_등록(final String name, final Long price) {
        final Product body = Product.of(name, price);
        return 상품_등록_요청(body).body()
                .as(Product.class);
    }

    protected ExtractableResponse<Response> 상품_등록_요청(final Object body) {
        return post("/api/products", body);
    }

    protected List<Product> 모든_상품_조회() {
        return 모든_상품_조회_요청().body()
                .jsonPath()
                .getList(".", Product.class);
    }

    protected ExtractableResponse<Response> 모든_상품_조회_요청() {
        return get("/api/products");
    }

    protected MenuGroup 메뉴_그룹_등록(final String name) {
        final MenuGroup body = new MenuGroup(name);
        return 메뉴_그룹_등록_요청(body).body()
                .as(MenuGroup.class);
    }

    protected ExtractableResponse<Response> 메뉴_그룹_등록_요청(final Object body) {
        return post("/api/menu-groups", body);
    }

    protected List<MenuGroup> 모든_메뉴_그룹_조회() {
        return 모든_메뉴_그룹_조회_요청().body()
                .jsonPath()
                .getList(".", MenuGroup.class);
    }

    protected ExtractableResponse<Response> 모든_메뉴_그룹_조회_요청() {
        return get("/api/menu-groups");
    }

    protected Menu 메뉴_등록(final String name, final Long price, final MenuGroup menuGroup, final Long... productIds) {
        final Menu body = Menu.create(name, price, menuGroup.getId(), List.of(productIds));
        return 메뉴_등록_요청(body).body()
                .as(Menu.class);
    }

    protected ExtractableResponse<Response> 메뉴_등록_요청(final Object body) {
        return post("/api/menus", body);
    }

    protected List<Menu> 모든_메뉴_조회() {
        return 모든_메뉴_조회_요청().body()
                .jsonPath()
                .getList(".", Menu.class);
    }

    protected ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return get("/api/menus");
    }

    protected OrderTable 테이블_등록(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.create();
        orderTable.enterGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return 테이블_등록_요청(orderTable).body()
                .as(OrderTable.class);
    }

    protected ExtractableResponse<Response> 테이블_등록_요청(final Object orderTable) {
        return post("/api/tables", orderTable);
    }

    public OrderTable 손님_채운_테이블_생성(final int numberOfGuests) {
        OrderTable orderTable = 테이블_등록(2, false);
        orderTable = 테이블_채움(orderTable.getId());
        orderTable = 테이블_손님_수_변경(orderTable.getId(), numberOfGuests);
        return orderTable;
    }

    public List<OrderTable> 테이블_전체_조회() {
        return 테이블_전체_조회_요청().body()
                .jsonPath()
                .getList(".", OrderTable.class);
    }

    protected ExtractableResponse<Response> 테이블_전체_조회_요청() {
        return get("/api/tables");
    }

    public OrderTable 테이블_손님_수_변경(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable(orderTableId, null, numberOfGuests, false);
        return 테이블_손님_수_변경_요청(orderTable).body()
                .as(OrderTable.class);
    }

    protected ExtractableResponse<Response> 테이블_손님_수_변경_요청(final OrderTable orderTable) {
        return put("/api/tables/" + orderTable.getId() + "/number-of-guests", orderTable);
    }

    public OrderTable 테이블_채움(final Long orderTableId) {
        final OrderTable orderTable = new OrderTable(orderTableId, null, 0, false);
        orderTable.setEmpty(false);
        return 테이블_empty_변경_요청(orderTable).body()
                .as(OrderTable.class);
    }

    public OrderTable 테이블_비움(final Long orderTableId) {
        final OrderTable orderTable = new OrderTable(orderTableId, null, 0, false);
        orderTable.setEmpty(true);
        return 테이블_empty_변경_요청(orderTable).body()
                .as(OrderTable.class);
    }

    protected ExtractableResponse<Response> 테이블_empty_변경_요청(final OrderTable orderTable) {
        return put("/api/tables/" + orderTable.getId() + "/empty", orderTable);
    }

    public TableGroup 테이블_그룹_생성(final OrderTable... tables) {
        final TableGroup tableGroup = TableGroup.of(List.of(tables));
        return 테이블_그룹_생성_요청(tableGroup).body()
                .as(TableGroup.class);
    }

    protected ExtractableResponse<Response> 테이블_그룹_생성_요청(final TableGroup tableGroup) {
        return post("/api/table-groups", tableGroup);
    }

    public void 테이블_그룹을_해제한다(final TableGroup tableGroup) {
        테이블_그룹_해제_요청(tableGroup);
    }

    protected ExtractableResponse<Response> 테이블_그룹_해제_요청(final TableGroup tableGroup) {
        return delete("/api/table-groups/" + tableGroup.getId());
    }

    public Order 메뉴_주문(final OrderTable orderTable, final Long... menuIds) {
        final Order order = Order.create(orderTable.getId(), List.of(menuIds));
        return 메뉴_주문_요청(order).body()
                .as(Order.class);
    }

    protected ExtractableResponse<Response> 메뉴_주문_요청(final Order order) {
        return post("/api/orders", order);
    }

    public List<Order> 모든_주문_조회() {
        return 모든_주문_조회_요청().body()
                .jsonPath()
                .getList(".", Order.class);
    }

    protected ExtractableResponse<Response> 모든_주문_조회_요청() {
        return get("/api/orders");
    }

    public Order 주문을_식사_상태로_만든다(final Order order) {
        order.changeOrderStatus(OrderStatus.MEAL.name());
        return 주문_상태_변경_요청(order).body()
                .as(Order.class);
    }

    public Order 주문을_완료_상태로_만든다(final Order order) {
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        return 주문_상태_변경_요청(order).body()
                .as(Order.class);
    }

    protected ExtractableResponse<Response> 주문_상태_변경_요청(final Order order) {
        return put("/api/orders/" + order.getId() + "/order-status", order);
    }

    protected <T> ExtractableResponse<Response> post(final String url, final T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> get(final String url) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse<Response> put(final String url, final T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse<Response> delete(final String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    protected void 응답_코드_일치_검증(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    protected <T> void 리스트_데이터_검증(final List<T> list, final String fieldName, final Object... expected) {
        assertThat(list).extracting(fieldName).contains(expected);
    }

    protected <T> void 단일_데이터_검증(final T actual, final T expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
