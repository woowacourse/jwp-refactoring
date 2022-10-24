package acceptance;

import static fixture.MenuFixtures.양념치킨_메뉴;
import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    private static class OrderLineItemRequest {

        private final long menuId;
        private final long quantity;

        public OrderLineItemRequest(long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // arrange
        changeTableEmptyStatus(1L, false);

        // act
        Order order = createOrder(1L,
                new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1),
                new OrderLineItemRequest(양념치킨_메뉴.id(), 2)
        );

        // assert
        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo("COOKING");
        assertThat(order.getOrderTableId()).isEqualTo(1L);
        assertThat(order.getOrderLineItems())
                .extracting(OrderLineItem::getMenuId, OrderLineItem::getQuantity)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        tuple(후라이드치킨_메뉴.id(), 1L),
                        tuple(양념치킨_메뉴.id(), 2L)
                );
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findOrders() {
        // arrange
        changeTableEmptyStatus(1L, false);
        Order order1 = createOrder(1L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1));

        changeTableEmptyStatus(2L, false);
        Order order2 = createOrder(2L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 2));

        changeTableEmptyStatus(3L, false);
        Order order3 = createOrder(3L, new OrderLineItemRequest(양념치킨_메뉴.id(), 3));

        // act
        List<Order> orders = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", Order.class);

        // assert
        assertOrders(orders, order1, order2, order3);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // arrange
        changeTableEmptyStatus(1L, false);
        Order order = createOrder(1L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1));

        // act
        Order changedOrder = changeOrderStatus(order.getId());

        // assert
        assertThat(changedOrder.getId()).isEqualTo(order.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }

    private Order changeOrderStatus(long id) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("order-id", id)
                .body(Map.of("orderStatus", "MEAL"))
                .when().log().all()
                .put("/api/orders/{order-id}/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Order.class);
    }

    private void assertOrders(List<Order> actualOrders, Order... expectedOrders) {
        assertThat(actualOrders).hasSize(expectedOrders.length);

        for (Order expectedOrder : expectedOrders) {
            Order actualOrder = actualOrders.stream()
                    .filter(o -> o.getId().equals(expectedOrder.getId()))
                    .findAny()
                    .orElseThrow();
            assertOrder(actualOrder, expectedOrder);
        }
    }

    private void assertOrder(Order actualOrder, Order expectedOrder) {
        List<Tuple> expectedOrderLineItems = expectedOrder.getOrderLineItems().stream()
                .map(item -> tuple(item.getMenuId(), item.getQuantity()))
                .collect(Collectors.toList());

        assertThat(actualOrder.getOrderLineItems())
                .extracting(OrderLineItem::getMenuId, OrderLineItem::getQuantity)
                .hasSize(expectedOrder.getOrderLineItems().size())
                .containsExactlyInAnyOrderElementsOf(expectedOrderLineItems);
        assertThat(actualOrder.getId()).isEqualTo(expectedOrder.getId());
        assertThat(actualOrder.getOrderTableId()).isEqualTo(expectedOrder.getOrderTableId());
        assertThat(actualOrder.getOrderStatus()).isEqualTo(expectedOrder.getOrderStatus());
        assertThat(actualOrder.getOrderedTime()).isEqualTo(expectedOrder.getOrderedTime());
    }

    private Order createOrder(long tableId, OrderLineItemRequest... itemRequests) {
        Map<String, Object> body = Map.of(
                "orderTableId", tableId,
                "orderLineItems", List.of(itemRequests)
        );

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Order.class);
    }

    private OrderTable changeTableEmptyStatus(long tableId, boolean tableStatus) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(Map.of("empty", tableStatus))
                .when().log().all()
                .put("/api/tables/{table-id}/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTable.class);
    }
}
