package kitchenpos.acceptance;

import static java.util.stream.Collectors.toList;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.ExceptionResponse;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusRequest;
import kitchenpos.ui.request.OrderTableGuestsRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.OrderResponse;
import kitchenpos.ui.response.OrderTableResponse;
import kitchenpos.ui.response.ProductResponse;
import kitchenpos.ui.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("주문테이블 인수 테스트")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/tables - 주문테이블을 생성할 때 단체 지정이 안된 상태로 생성된다.")
    @Test
    void createWithTableGroupIdNull() {
        // given
        OrderTableRequest request = new OrderTableRequest(5, false);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/tables")
            .then().log().all()
            .statusCode(CREATED.value())
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isNotNull();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/tables - 모든 주문테이블을 조회한다.")
    @Test
    void findAll() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<OrderTableResponse> orderTables = response.jsonPath().getList(".", OrderTableResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(orderTables).isNotNull();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty - 주문테이블을 빈 상태로 변경할 때")
    @Nested
    class ChangeStatusEmpty {

        @DisplayName("주문테이블이 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            HTTP_요청을_통해_OrderTable을_생성한다(1, false);
            OrderTable emptyOrderTable = OrderTable을_생성한다(1, true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", -1L))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 이미 단체로 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void tableGroupIdNonNullException() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            OrderTable emptyOrderTable = OrderTable을_생성한다(3, true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable1.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블에 조리 상태인 주문이 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndCookingStatusOrder() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(COOKING, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(1, true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블에 식사 상태인 주문이 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndMealStatusOrder() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(MEAL, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(1, true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블에 모든 주문이 완료 상태인 경우 상태가 변경된다.")
        @Test
        void existOrderTableIdMatchAndCompletionStatusOrder() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(COMPLETION, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(1, true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(OK.value())
                .extract();

            OrderTableResponse changedOrderTable = response.as(OrderTableResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrderTable.getId()).isEqualTo(orderTable.getId());
            assertThat(changedOrderTable.isEmpty()).isEqualTo(emptyOrderTable.isEmpty());
        }
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests - 손님 수를 변경할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("변경할 손님 수가 음수면 예외가 발생한다.")
        @Test
        void numberOfGuestsNegativeException() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            OrderTableGuestsRequest request = new OrderTableGuestsRequest(-5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(String.format("/api/tables/%d/number-of-guests", orderTable.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderTableByIdException() {
            // given
            OrderTableGuestsRequest request = new OrderTableGuestsRequest(5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(String.format("/api/tables/%d/number-of-guests", -1L))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 이미 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void alreadyEmptyStatusOrderTableException() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTable changeNumberOfGuestsOrderTable = OrderTable을_생성한다(5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsOrderTable)
                .when().put(String.format("/api/tables/%d/number-of-guests", orderTable.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 비어있는 상태가 아니고, 조회가 가능하며 변경할 손님 수가 음수가 아닌 경우 변경에 성공한다.")
        @Test
        void success() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            OrderTable changeNumberOfGuestsOrderTable = OrderTable을_생성한다(5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsOrderTable)
                .when().put(String.format("/api/tables/%d/number-of-guests", orderTable.getId()))
                .then().log().all()
                .statusCode(OK.value())
                .extract();

            OrderTableResponse changedOrderTable = response.as(OrderTableResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrderTable.getId()).isEqualTo(orderTable.getId());
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(changeNumberOfGuestsOrderTable.getNumberOfGuests());
        }
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        return OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }

    private TableGroupResponse HTTP_요청을_통해_TableGroup을_생성한다(OrderTableResponse... orderTables) {
        List<Long> orderTableIds = Arrays.stream(orderTables)
            .map(OrderTableResponse::getId)
            .collect(toList());

        TableGroupRequest request = new TableGroupRequest(orderTableIds);

        return postRequestWithBody("/api/table-groups", request).as(TableGroupResponse.class);
    }

    private OrderTableResponse HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, isEmpty);

        return postRequestWithBody("/api/tables", request).as(OrderTableResponse.class);
    }

    private OrderResponse HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(OrderStatus orderStatus, OrderTableResponse orderTable) {
        MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

        ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
        ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);
        MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
        MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
        List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
        MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

        OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu1.getId(), 1);
        OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu2.getId(), 1);

        OrderRequest order = OrderRequest를_생성한다(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

        OrderResponse createdOrder = postRequestWithBody("/api/orders", order).as(OrderResponse.class);
        OrderStatusRequest statusRequest = Status_변화용_OrderRequest를_생성한다(orderStatus);

        return putRequestWithBody(String.format("api/orders/%d/order-status", createdOrder.getId()), statusRequest)
            .as(OrderResponse.class);
    }

    private OrderRequest OrderRequest를_생성한다(OrderTableResponse orderTable, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTable.getId(), orderLineItems);
    }

    private OrderStatusRequest Status_변화용_OrderRequest를_생성한다(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus.name());
    }

    private OrderTableResponse HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests) {
        return HTTP_요청을_통해_OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderLineItemRequest OrderLineItemRequest을_생성한다(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    private MenuResponse HTTP_요청을_통해_Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        MenuRequest request = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return postRequestWithBody("/api/menus", request).as(MenuResponse.class);
    }

    private MenuGroup HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProductRequest MenuProductRequest를_생성한다(ProductResponse productResponse, long quantity) {
        return new MenuProductRequest(productResponse.getId(), quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
