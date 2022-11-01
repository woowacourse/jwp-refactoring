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
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.response.OrderResponse;
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
        final OrderTable orderTable = OrderTable.of(1, false);
        final OrderTable savedOrderTable = saveOrderTable(orderTable);

        final OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), "COOKING", LocalDateTime.now(),
                List.of(createOrderLineItem()));

        // when
        final OrderResponse response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(COOKING.name());
        assertThat(response)
                .extracting(OrderResponse::getOrderTableId, OrderResponse::getOrderStatus)
                .containsExactly(orderRequest.getOrderTableId(), orderRequest.getOrderStatus());
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = saveOrderTable(1, false);
        final OrderTable anotherOrderTable = saveOrderTable(1, false);

        final OrderRequest orderRequest1 = new OrderRequest(orderTable.getId(), "COOKING", LocalDateTime.now(),
                List.of(createOrderLineItem()));
        final OrderRequest orderRequest2 = new OrderRequest(anotherOrderTable.getId(), "COOKING", LocalDateTime.now(),
                List.of(createOrderLineItem()));

        final OrderResponse savedOrder1 = saveOrder(orderRequest1);
        final OrderResponse savedOrder2 = saveOrder(orderRequest2);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        final List<OrderResponse> orders = getOrders(response);

        // then
        assertThat(orders)
                .hasSize(2)
                .filteredOn(it -> it.getId() != null)
                .extracting(OrderResponse::getId, OrderResponse::getOrderTableId, OrderResponse::getOrderStatus)
                .containsExactlyInAnyOrder(
                        tuple(savedOrder1.getId(), savedOrder1.getOrderTableId(), savedOrder1.getOrderStatus()),
                        tuple(savedOrder2.getId(), savedOrder2.getOrderTableId(), savedOrder2.getOrderStatus())
                );
    }

    @DisplayName("특정 주문의 주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderTable orderTable = OrderTable.of(1, false);
        final OrderTable savedOrderTable = saveOrderTable(orderTable);

        final OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), "COOKING", LocalDateTime.now(),
                List.of(createOrderLineItem()));
        final OrderResponse savedOrder = saveOrder(orderRequest);
        final OrderRequest changeOrder = new OrderRequest(savedOrderTable.getId(), "COMPLETION", LocalDateTime.now(),
                List.of(createOrderLineItem()));

        // when
        final OrderResponse response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("orderId", savedOrder.getId())
                .body(changeOrder)
                .when().log().all()
                .put("/api/orders/{orderId}/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderResponse.class);

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

    private static OrderResponse saveOrder(final OrderRequest request) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderResponse.class);
    }

    private OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);
    }

    private static List<OrderResponse> getOrders(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", OrderResponse.class);
    }

    private OrderLineItemRequest createOrderLineItem() {
        return new OrderLineItemRequest(SEQUENCE, ORDER_ID, MENU_ID, QUANTITY);
    }
}
