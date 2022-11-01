package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.application.dto.TableDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.ui.dto.ChangeTableEmptyRequest;
import kitchenpos.ui.dto.CreateMenuGroupRequest;
import kitchenpos.ui.dto.CreateMenuProductRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import kitchenpos.ui.dto.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.CreateOrderRequest;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.CreateProductRequest;
import kitchenpos.ui.dto.CraeteTableGroupRequest;
import kitchenpos.ui.dto.TableGuestNumberRequest;
import kitchenpos.ui.dto.CreateTableRequest;
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

    protected ProductDto 토마토파스타;
    protected ProductDto 목살스테이크;
    protected ProductDto 탄산음료;

    protected MenuGroupDto 파스타;
    protected MenuGroupDto 스테이크;
    protected MenuGroupDto 음료;
    protected MenuGroupDto 세트;

    protected MenuDto 파스타한상;

    protected TableDto 빈_테이블1;
    protected TableDto 빈_테이블2;

    protected TableDto 손님있는_식사중_테이블;
    protected TableDto 손님있는_테이블;

    protected OrderDto 식사중인_주문;

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
        파스타한상 = 메뉴_등록("파스타한상", 35000L, 세트.getId(), 토마토파스타.getId(), 목살스테이크.getId(), 탄산음료.getId());
        빈_테이블1 = 테이블_등록(0, true);
        빈_테이블2 = 테이블_등록(0, true);
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        식사중인_주문 = 메뉴_주문(손님있는_식사중_테이블.getId(), 파스타한상.getId());
        식사중인_주문 = 주문을_식사_상태로_만든다(식사중인_주문.getId());
    }

    protected ProductDto 상품_등록(final String name, final Long price) {
        final CreateProductRequest requestBody = new CreateProductRequest(name, BigDecimal.valueOf(price));
        return 상품_등록_요청(requestBody).body()
                .as(ProductDto.class);
    }

    protected ExtractableResponse<Response> 상품_등록_요청(final Object body) {
        return post("/api/products", body);
    }

    protected List<ProductDto> 모든_상품_조회() {
        return 모든_상품_조회_요청().body()
                .jsonPath()
                .getList(".", ProductDto.class);
    }

    protected ExtractableResponse<Response> 모든_상품_조회_요청() {
        return get("/api/products");
    }

    protected MenuGroupDto 메뉴_그룹_등록(final String name) {
        return 메뉴_그룹_등록_요청(name).body()
                .as(MenuGroupDto.class);
    }

    protected ExtractableResponse<Response> 메뉴_그룹_등록_요청(final String name) {
        final CreateMenuGroupRequest requestBody = new CreateMenuGroupRequest(name);
        return post("/api/menu-groups", requestBody);
    }

    protected List<MenuGroupDto> 모든_메뉴_그룹_조회() {
        return 모든_메뉴_그룹_조회_요청().body()
                .jsonPath()
                .getList(".", MenuGroupDto.class);
    }

    protected ExtractableResponse<Response> 모든_메뉴_그룹_조회_요청() {
        return get("/api/menu-groups");
    }

    protected MenuDto 메뉴_등록(final String name, final Long price, final Long menuGroupId, final Long... productIds) {
        return 메뉴_등록_요청(name, price, menuGroupId, List.of(productIds)).body()
                .as(MenuDto.class);
    }

    protected ExtractableResponse<Response> 메뉴_등록_요청(final String name, final Long price, final Long menuGroupId, final List<Long> productIds) {
        final List<MenuProduct> menuProducts = productIds.stream()
                .map(MenuProduct::new)
                .collect(Collectors.toList());
        final Menu menu = Menu.create(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
        final List<CreateMenuProductRequest> createMenuProductRequests = menu.getMenuProducts()
                .stream()
                .map(menuProduct -> new CreateMenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
        final CreateMenuRequest requestBody = new CreateMenuRequest(
                name,
                BigDecimal.valueOf(price),
                menuGroupId,
                createMenuProductRequests
        );
        return post("/api/menus", requestBody);
    }

    protected List<MenuDto> 모든_메뉴_조회() {
        return 모든_메뉴_조회_요청().body()
                .jsonPath()
                .getList(".", MenuDto.class);
    }

    protected ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return get("/api/menus");
    }

    protected TableDto 테이블_등록(final int numberOfGuests, final boolean empty) {
        final CreateTableRequest body = new CreateTableRequest(
                numberOfGuests,
                empty
        );
        return 테이블_등록_요청(body).body()
                .as(TableDto.class);
    }

    protected ExtractableResponse<Response> 테이블_등록_요청(final Object orderTable) {
        return post("/api/tables", orderTable);
    }

    public TableDto 손님_채운_테이블_생성(final int numberOfGuests) {
        TableDto orderTable = 테이블_등록(2, false);
        orderTable = 테이블_채움(orderTable.getId());
        orderTable = 테이블_손님_수_변경(orderTable.getId(), numberOfGuests);
        return orderTable;
    }

    protected ExtractableResponse<Response> 테이블_전체_조회_요청() {
        return get("/api/tables");
    }

    public TableDto 테이블_손님_수_변경(final Long orderTableId, final int numberOfGuests) {
        return 테이블_손님_수_변경_요청(orderTableId, numberOfGuests).body()
                .as(TableDto.class);
    }

    protected ExtractableResponse<Response> 테이블_손님_수_변경_요청(final Long orderTableId, final int numberOfGuests) {
        final TableGuestNumberRequest requestBody = new TableGuestNumberRequest(numberOfGuests);
        return put("/api/tables/" + orderTableId + "/number-of-guests", requestBody);
    }

    public TableDto 테이블_채움(final Long orderTableId) {
        return 테이블_empty_변경_요청(orderTableId, false).body()
                .as(TableDto.class);
    }

    public TableDto 테이블_비움(final Long orderTableId) {
        return 테이블_empty_변경_요청(orderTableId, true).body()
                .as(TableDto.class);
    }

    protected ExtractableResponse<Response> 테이블_empty_변경_요청(final Long orderTableId,
                                                            final Boolean empty) {
        final ChangeTableEmptyRequest requestBody = new ChangeTableEmptyRequest(empty);
        return put("/api/tables/" + orderTableId + "/empty", requestBody);
    }

    public TableGroupDto 테이블_그룹_생성(final TableDto... tables) {
        final List<Long> tableIds = Arrays.stream(tables)
                .map(TableDto::getId)
                .collect(Collectors.toList());
        return 테이블_그룹_생성_요청(tableIds).body()
                .as(TableGroupDto.class);
    }

    protected ExtractableResponse<Response> 테이블_그룹_생성_요청(final List<Long> tableIds) {
        final List<OrderTableIdRequest> orderTableIdRequests = tableIds.stream()
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
        final CraeteTableGroupRequest requestBody = new CraeteTableGroupRequest(orderTableIdRequests);
        return post("/api/table-groups", requestBody);
    }

    protected ExtractableResponse<Response> 테이블_그룹_해제_요청(final Long tableGroupId) {
        return delete("/api/table-groups/" + tableGroupId);
    }

    public OrderDto 메뉴_주문(final Long orderTableId, final Long... menuIds) {
        final List<OrderLineItem> orderLineItems = Arrays.stream(menuIds)
                .map(menuId -> new OrderLineItem(menuId, 1))
                .collect(Collectors.toList());
        final Order order = Order.create(orderTableId, orderLineItems);
        return 메뉴_주문_요청(order).body()
                .as(OrderDto.class);
    }

    protected ExtractableResponse<Response> 메뉴_주문_요청(final Order order) {
        final List<CreateOrderLineItemRequest> createOrderLineItemRequests = order.getOrderLineItems().stream()
                .map(orderLineItem -> new CreateOrderLineItemRequest(orderLineItem.getMenuId(),
                        orderLineItem.getQuantity())).collect(
                        Collectors.toList());
        final CreateOrderRequest requestBody = new CreateOrderRequest(
                order.getOrderTableId(),
                createOrderLineItemRequests
        );
        return post("/api/orders", requestBody);
    }

    public List<OrderDto> 모든_주문_조회() {
        return 모든_주문_조회_요청().body()
                .jsonPath()
                .getList(".", OrderDto.class);
    }

    protected ExtractableResponse<Response> 모든_주문_조회_요청() {
        return get("/api/orders");
    }

    public OrderDto 주문을_식사_상태로_만든다(final Long orderId) {
        return 주문_상태_변경_요청(orderId, OrderStatus.MEAL.name()).body()
                .as(OrderDto.class);
    }

    public OrderDto 주문을_완료_상태로_만든다(final Long orderId) {
        return 주문_상태_변경_요청(orderId, OrderStatus.COMPLETION.name()).body()
                .as(OrderDto.class);
    }

    protected ExtractableResponse<Response> 주문_상태_변경_요청(final Long orderId, final String orderStatus) {
        final ChangeOrderStatusRequest requestBody = new ChangeOrderStatusRequest(orderStatus);
        return put("/api/orders/" + orderId + "/order-status", requestBody);
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
