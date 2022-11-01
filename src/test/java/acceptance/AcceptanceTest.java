package acceptance;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.Application;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.request.OrderChangeRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.request.OrderRequest;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.request.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {Application.class, DataClearExtension.class}
)
@ExtendWith(DataClearExtension.class)
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected long 상품을_생성한다(String name, int price) {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(new ProductRequest(name, BigDecimal.valueOf(price)))
                .when().log().all()
                .post("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected List<ProductResponse> 상품을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", ProductResponse.class);
    }

    protected long 메뉴_그룹을_생성한다(String name) {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("name", name))
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getLong("id");
    }


    protected List<MenuGroupResponse> 메뉴_그룹을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", MenuGroupResponse.class);
    }

    protected long 메뉴를_생성한다(String name, int price, long menuGroup, List<Long> products, int quantity) {
        List<MenuProductRequest> menuProducts = products.stream()
                .map(product -> new MenuProductRequest(product, quantity))
                .collect(Collectors.toList());

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(new MenuRequest(name, BigDecimal.valueOf(price), menuGroup, menuProducts))
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getLong("id");
    }

    protected List<MenuResponse> 메뉴를_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .extract().body().jsonPath().getList(".", MenuResponse.class);
    }

    protected long 테이블을_생성한다(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTable = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected List<OrderTableResponse> 테이블_목록을_조회한다() {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderTableResponse.class);
    }

    protected OrderTableResponse 테이블_상태를_변경한다(long orderTableId, boolean empty) {
        OrderTableRequest orderTable = new OrderTableRequest(0, empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .put("/api/tables/{order_table_id}/empty", orderTableId)
                .then().log().all()
                .extract().as(OrderTableResponse.class);
    }

    protected TableGroup 테이블_그룹을_생성한다(List<Long> orderTableId) {
        TableGroupRequest tableGroup = new TableGroupRequest(orderTableId);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    protected void 테이블_그룹을_해제한다(Long id) {
        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .when().log().all()
                .delete("/api/table-groups/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    protected OrderTableResponse 테이블_방문자_수를_변경한다(long orderTableId, int numberOfGuests) {
        OrderTableRequest orderTable = new OrderTableRequest(2, false);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .put("/api/tables/{order_table_id}/number-of-guests", orderTableId)
                .then().log().all()
                .extract().as(OrderTableResponse.class);
    }

    protected long 주문을_생성한다(long table, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest orderRequest = new OrderRequest(table, orderLineItems);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    protected OrderResponse 주문_상태를_변경한다(long orderId, String status) {
        OrderChangeRequest orderChangeRequest = new OrderChangeRequest(status);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(orderChangeRequest)
                .when().log().all()
                .put("/api/orders/{order_id}/order-status", orderId)
                .then().log().all()
                .extract().as(OrderResponse.class);
    }

    protected List<OrderResponse> 주문_목목을_조회한다() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .extract().jsonPath().getList(".", OrderResponse.class);
    }
}
