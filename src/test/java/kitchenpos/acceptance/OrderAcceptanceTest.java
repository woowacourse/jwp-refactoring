package kitchenpos.acceptance;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.ExceptionResponse;
import kitchenpos.ui.request.MenuGroupRequest;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.OrderResponse;
import kitchenpos.ui.response.OrderTableResponse;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/orders - 주문을 생성할 때")
    @Nested
    class Post {

        @DisplayName("정상적인 경우 주문 생성에 성공한다.")
        @Test
        void createPost() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(2);
            MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu1.getId(), 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu2.getId(), 1);

            OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }

        @DisplayName("주문항목이 없다면 예외가 발생한다.")
        @Test
        void noOrderLineItem() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(2);
            MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            OrderRequest request = OrderRequest를_생성한다(orderTable, new ArrayList<>());

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("동일한 메뉴를 추가할 때 수량이 아닌 주문항목을 새로 추가해서, 주문항목 개수와 메뉴 개수가 다를 경우 예외가 발생한다.")
        @Test
        void orderLineItemCountNonMatchWithMenu() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(2);
            MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu.getId(), 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu.getId(), 1);

            OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문 테이블이 실제로 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void orderTableNotFound() {
            // given
            MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu1.getId(), 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu2.getId(), 1);

            OrderTableResponse 없는_테이블 = new OrderTableResponse(-1L, 1L, 5, true);
            OrderRequest request = OrderRequest를_생성한다(없는_테이블, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 이미 비어있을 경우 예외가 발생한다.")
        @Test
        void orderTableStatusEmpty() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu1.getId(), 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu2.getId(), 1);

            OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }
    }

    @DisplayName("GET /api/orders - 모든 주문 목록을 반환 받는다.")
    @Test
    void createGet() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<Order> orders = response.jsonPath().getList(".", Order.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(orders).isNotNull();
    }

    @DisplayName("PUT /api/orders/{orderId}/order-status - 주문의 상태를 변경할 때")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("주문이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderIdException() {
            // given
            OrderStatusRequest request = Status_변화용_OrderRequest를_생성한다(COOKING);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(String.format("/api/orders/%d/order-status", -1L))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문이 이미 완료 상태일 경우 예외가 발생한다.")
        @Test
        void alreadyCompletionStatusException() {
            // given
            OrderResponse order = HTTP_요청을_통해_Order를_생성한다();
            OrderStatusRequest request = Status_변화용_OrderRequest를_생성한다(COMPLETION);

            putRequestWithBody(String.format("/api/orders/%d/order-status", order.getId()), request);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(String.format("/api/orders/%d/order-status", order.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문의 상태가 정상적으로 변경되고 반환된다.")
        @Test
        void success() {
            // given
            OrderResponse order = HTTP_요청을_통해_Order를_생성한다();
            OrderStatusRequest request = Status_변화용_OrderRequest를_생성한다(COMPLETION);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(String.format("/api/orders/%d/order-status", order.getId()))
                .then().log().all()
                .statusCode(OK.value())
                .extract();

            OrderResponse changedOrder = response.as(OrderResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrder.getId()).isEqualTo(order.getId());
            assertThat(changedOrder.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }
    }

    private OrderResponse HTTP_요청을_통해_Order를_생성한다() {
        OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(2);
        MenuGroupResponse menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

        ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
        ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);
        MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
        MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
        List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
        MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

        OrderLineItemRequest orderLineItem1 = OrderLineItemRequest을_생성한다(menu1.getId(), 1);
        OrderLineItemRequest orderLineItem2 = OrderLineItemRequest을_생성한다(menu2.getId(), 1);

        OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

        return postRequestWithBody("/api/orders", request).as(OrderResponse.class);
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

    private OrderTableResponse HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, isEmpty);

        return postRequestWithBody("/api/tables", request).as(OrderTableResponse.class);
    }

    private OrderLineItemRequest OrderLineItemRequest을_생성한다(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    private MenuResponse HTTP_요청을_통해_Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        MenuRequest request = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return postRequestWithBody("/api/menus", request).as(MenuResponse.class);
    }

    private MenuGroupResponse HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);

        return postRequestWithBody("/api/menu-groups", request).as(MenuGroupResponse.class);
    }

    private MenuProductRequest MenuProductRequest를_생성한다(ProductResponse product, long quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
