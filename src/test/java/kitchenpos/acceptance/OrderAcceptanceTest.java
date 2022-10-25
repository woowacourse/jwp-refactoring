package kitchenpos.acceptance;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    private static final long QUANTITY = 1L;
    private static final long MENU_ID = 1L;
    private static final long ORDER_ID = 1L;
    private static final long SEQUENCE = 1L;

    @DisplayName("새로운 주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable savedOrderTable = saveOrderTable(orderTable);
        final OrderLineItem orderLineItem = createOrderLineItem();

        final Order order = new Order(savedOrderTable.getId(), "COOKING", LocalDateTime.now(), List.of(orderLineItem));

        // when
        final Order response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(order)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Order.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(COOKING.name());
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems", "orderedTime")
                .isEqualTo(order);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = saveOrderTable(1, false);
        final OrderTable anotherOrderTable = saveOrderTable(1, false);

        final Order order1 = new Order(orderTable.getId(), "COOKING", LocalDateTime.now(), List.of(createOrderLineItem()));
        final Order order2 = new Order(anotherOrderTable.getId(), "COOKING", LocalDateTime.now(), List.of(createOrderLineItem()));

        final Order savedOrder1 = saveOrder(order1);
        final Order savedOrder2 = saveOrder(order2);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        final List<Order> orders = getOrders(response);

        // then
        assertThat(orders)
                .hasSize(2)
                .filteredOn(it -> it.getId() != null)
                .extracting(Order::getId, Order::getOrderTableId, Order::getOrderStatus)
                .containsExactlyInAnyOrder(
                        tuple(savedOrder1.getId(), savedOrder1.getOrderTableId(), savedOrder1.getOrderStatus()),
                        tuple(savedOrder2.getId(), savedOrder2.getOrderTableId(), savedOrder2.getOrderStatus())
                );
    }

    @DisplayName("특정 주문의 주문 상태를 조회할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable savedOrderTable = saveOrderTable(orderTable);
        final OrderLineItem orderLineItem = createOrderLineItem();

        final Order order = new Order(savedOrderTable.getId(), "COOKING", LocalDateTime.now(), List.of(orderLineItem));
        final Order savedOrder = saveOrder(order);
        final Order changeOrder = new Order(savedOrderTable.getId(), "COMPLETION", LocalDateTime.now(),
                List.of(orderLineItem));

        // when
        final Order response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("orderId", savedOrder.getId())
                .body(changeOrder)
                .when().log().all()
                .put("/api/orders/{orderId}/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Order.class);

        // then
        assertThat(response.getOrderStatus()).isEqualTo(COMPLETION.name());
    }

    private static OrderTable saveOrderTable(final OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);
    }

    private static Order saveOrder(final Order order) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(order)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Order.class);
    }

    private OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);
    }

    private static List<Order> getOrders(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", Order.class);
    }

    private OrderLineItem createOrderLineItem() {
        return new OrderLineItem(SEQUENCE, ORDER_ID, MENU_ID, QUANTITY);
    }
}
