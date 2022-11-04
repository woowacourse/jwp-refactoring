package acceptance;

import static fixture.MenuFixtures.양념치킨_메뉴;
import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import common.AcceptanceTest;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.response.OrderLineItemResponse;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.order.ui.response.OrderTableResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // arrange
        changeTableEmptyStatus(1L, false);

        // act
        OrderResponse order = createOrder(1L,
                new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1),
                new OrderLineItemRequest(양념치킨_메뉴.id(), 2)
        );

        // assert
        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(COOKING.name());
        assertThat(order.getOrderTableId()).isEqualTo(1L);
        assertThat(order.getOrderLineItems())
                .extracting(OrderLineItemResponse::getName, r -> r.getPrice().intValue(), OrderLineItemResponse::getQuantity)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        tuple(후라이드치킨_메뉴.이름(), 후라이드치킨_메뉴.가격(), 1L),
                        tuple(양념치킨_메뉴.이름(), 양념치킨_메뉴.가격(), 2L)
                );
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findOrders() {
        // arrange
        changeTableEmptyStatus(1L, false);
        OrderResponse expectedOrder1 = createOrder(1L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1));

        changeTableEmptyStatus(2L, false);
        OrderResponse expectedOrder2 = createOrder(2L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 2));

        changeTableEmptyStatus(3L, false);
        OrderResponse expectedOrder3 = createOrder(3L, new OrderLineItemRequest(양념치킨_메뉴.id(), 3));

        // act
        List<OrderResponse> actualOrders = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderResponse.class);

        // assert
        assertOrdersContainsExactlyInAnyOrder(actualOrders, expectedOrder1, expectedOrder2, expectedOrder3);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // arrange
        changeTableEmptyStatus(1L, false);
        OrderResponse order = createOrder(1L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1));

        // act
        OrderResponse changedOrder = changeOrderStatus(order.getId(), "MEAL");

        // assert
        assertThat(changedOrder.getId()).isEqualTo(order.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    private OrderResponse changeOrderStatus(long id, String status) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("order-id", id)
                .body(Map.of("orderStatus", status))
                .when().log().all()
                .put("/api/orders/{order-id}/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderResponse.class);
    }

    private void assertOrdersContainsExactlyInAnyOrder(List<OrderResponse> actualOrders, OrderResponse... expectedOrders) {
        assertThat(actualOrders).hasSize(expectedOrders.length);

        for (OrderResponse expectedOrder : expectedOrders) {
            OrderResponse actualOrder = actualOrders.stream()
                    .filter(o -> o.getId() == expectedOrder.getId())
                    .findAny()
                    .orElseThrow();
            assertOrder(actualOrder, expectedOrder);
        }
    }

    private void assertOrder(OrderResponse actualOrder, OrderResponse expectedOrder) {
        List<Tuple> expectedOrderLineItems = expectedOrder.getOrderLineItems().stream()
                .map(item -> tuple(item.getName(), item.getPrice().intValue(), item.getQuantity()))
                .collect(Collectors.toList());

        assertThat(actualOrder.getOrderLineItems())
                .extracting(OrderLineItemResponse::getName, r -> r.getPrice().intValue(), OrderLineItemResponse::getQuantity)
                .hasSize(expectedOrder.getOrderLineItems().size())
                .containsExactlyInAnyOrderElementsOf(expectedOrderLineItems);
        assertThat(actualOrder.getId()).isEqualTo(expectedOrder.getId());
        assertThat(actualOrder.getOrderTableId()).isEqualTo(expectedOrder.getOrderTableId());
        assertThat(actualOrder.getOrderStatus()).isEqualTo(expectedOrder.getOrderStatus());
        assertThat(actualOrder.getOrderedTime()).isEqualTo(expectedOrder.getOrderedTime());
    }

    private OrderResponse createOrder(long tableId, OrderLineItemRequest... itemRequests) {
        OrderRequest request = new OrderRequest(tableId, List.of(itemRequests));

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderResponse.class);
    }

    private OrderTableResponse changeTableEmptyStatus(long tableId, boolean tableStatus) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(Map.of("empty", tableStatus))
                .when().log().all()
                .put("/api/tables/{table-id}/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTableResponse.class);
    }
}
