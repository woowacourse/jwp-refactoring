package kitchenpos.acceptance;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Table 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/tables - OrderTable을 저장할 때 TableGroupId가 Null인 상태로 저장된다.")
    @Test
    void createWithTableGroupIdNull() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all()
            .statusCode(CREATED.value())
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isNotNull();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/tables - 모든 OrderTable을 조회한다.")
    @Test
    void findAll() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<OrderTable> orderTables = response.jsonPath().getList(".", OrderTable.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(orderTables).isNotNull();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty")
    @Nested
    class ChangeStatusEmpty {

        @DisplayName("OrderTableId와 일치하는 OrderTable이 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            HTTP_요청을_통해_OrderTable을_생성한다(1, false);
            OrderTable emptyOrderTable = OrderTable을_생성한다(true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", -1L))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTable에 TableGroupId가 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void tableGroupIdNonNullException() {
            // given
            OrderTable orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTable orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            OrderTable emptyOrderTable = OrderTable을_생성한다(true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable1.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTableId가 일치하고, COOKING 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndCookingStatusOrder() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(COOKING, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTableId가 일치하고, MEAL 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndMealStatusOrder() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(MEAL, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTableId가 일치하고, COMPLETION 상태인 Order가 존재하는 경우 상태가 변경된다.")
        @Test
        void existOrderTableIdMatchAndCompletionStatusOrder() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(COMPLETION, orderTable);

            OrderTable emptyOrderTable = OrderTable을_생성한다(true);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emptyOrderTable)
                .when().put(String.format("/api/tables/%d/empty", orderTable.getId()))
                .then().log().all()
                .statusCode(OK.value())
                .extract();

            OrderTable changedOrderTable = response.as(OrderTable.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrderTable.getId()).isEqualTo(orderTable.getId());
            assertThat(changedOrderTable.isEmpty()).isEqualTo(emptyOrderTable.isEmpty());
        }
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("손님 수가 음수면 예외가 발생한다.")
        @Test
        void numberOfGuestsNegativeException() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            OrderTable changeNumberOfGuestsOrderTable = OrderTable을_생성한다(-1);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsOrderTable)
                .when().put(String.format("/api/tables/%d/number-of-guests", orderTable.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("orderTableId와 일치하는 OrderTable이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderTableByIdException() {
            // given
            OrderTable changeNumberOfGuestsOrderTable = OrderTable을_생성한다(5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsOrderTable)
                .when().put(String.format("/api/tables/%d/number-of-guests", -1L))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTable이 이미 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void alreadyEmptyStatusOrderTableException() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTable changeNumberOfGuestsOrderTable = OrderTable을_생성한다(5);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsOrderTable)
                .when().put(String.format("/api/tables/%d/number-of-guests", orderTable.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("OrderTable이 비어있는 상태가 아니고, ID를 통해 조회할 수 있으며 손님 수가 음수가 아닌 경우 변경에 성공한다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
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

            OrderTable changedOrderTable = response.as(OrderTable.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrderTable.getId()).isEqualTo(orderTable.getId());
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(changeNumberOfGuestsOrderTable.getNumberOfGuests());
        }
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return orderTable;
    }

    private OrderTable OrderTable을_생성한다(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    private TableGroup HTTP_요청을_통해_TableGroup을_생성한다(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(orderTables));

        return postRequestWithBody("/api/table-groups", tableGroup).as(TableGroup.class);
    }

    private OrderTable HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        return HTTP_요청을_통해_OrderTable을_생성한다(numberOfGuests, isEmpty, null);
    }

    private OrderTable HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests, boolean isEmpty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        orderTable.setTableGroupId(tableGroupId);

        return postRequestWithBody("/api/tables", orderTable).as(OrderTable.class);
    }

    private Order HTTP_요청을_통해_Order를_생성하고_상태를_변화시킨다(OrderStatus orderStatus, OrderTable orderTable) {
        MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

        ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
        ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);
        MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
        MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
        List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
        MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

        OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
        OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

        Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        Order createdOrder = postRequestWithBody("/api/orders", order).as(Order.class);
        Order cookingOrder = Status_변화용_Order를_생성한다(orderStatus);

        return putRequestWithBody(String.format("api/orders/%d/order-status", createdOrder.getId()), cookingOrder).as(Order.class);
    }

    private Order Order를_생성한다(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    private Order Status_변화용_Order를_생성한다(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());

        return order;
    }

    private OrderTable HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests) {
        return HTTP_요청을_통해_OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderLineItem OrderLineItem을_생성한다(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private MenuResponse HTTP_요청을_통해_Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        MenuRequest request = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, MenuProductRequest.of(menuProducts));

        return postRequestWithBody("/api/menus", request).as(MenuResponse.class);
    }

    private MenuGroup HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProduct MenuProduct를_생성한다(ProductResponse productResponse, long quantity) {
        Product product = new Product(productResponse.getId(), productResponse.getName(), productResponse.getPrice());

        return new MenuProduct(product, quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
