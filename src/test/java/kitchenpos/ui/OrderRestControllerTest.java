package kitchenpos.ui;

import io.restassured.response.Response;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

class OrderRestControllerTest extends AcceptanceTest {
    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        Long tableId = createTable();
        OrderLineItemRequest orderItemRequest1 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderItemRequest2 = new OrderLineItemRequest(2L, 1L);

        OrderRequest orderRequest = new OrderRequest(tableId, Arrays.asList(orderItemRequest1, orderItemRequest2));

        // when
        Response response = post("/api/orders", orderRequest);

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("orderStatus", is("COOKING"))
                .body("orderLineItems.menuId", hasItems(
                        orderItemRequest1.getMenuId().intValue(),
                        orderItemRequest2.getMenuId().intValue()))
                .body("orderLineItems.quantity", hasItems(
                        orderItemRequest1.getQuantity().intValue(),
                        orderItemRequest2.getQuantity().intValue()));
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        Long order1Id = createOrder();
        Long order2Id = createOrder();

        // when, then
        get("/api/orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(
                        order1Id.intValue(),
                        order2Id.intValue()
                ));
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("주문 상태를 변경한다.")
    void changeStatus(OrderStatus expectedStatus) {
        // given
        Long orderId = createOrder();
        OrderRequest orderStatusRequest = new OrderRequest(expectedStatus.name());

        // when
        Response response = put("/api/orders/" + orderId + "/order-status", orderStatusRequest);

        // then
        response.then().body("orderStatus", is(expectedStatus.name()));
    }

    private Long createTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        return post("/api/tables", orderTableRequest)
                .then()
                .extract()
                .as(OrderTableResponse.class)
                .getId();
    }

    private Long createOrder() {
        Long tableId = createTable();
        OrderLineItemRequest orderItemRequest1 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderItemRequest2 = new OrderLineItemRequest(2L, 1L);

        OrderRequest orderRequest = new OrderRequest(tableId, Arrays.asList(orderItemRequest1, orderItemRequest2));

        Response response = post("/api/orders", orderRequest);

        return response.then().extract().as(OrderResponse.class).getId();
    }
}
