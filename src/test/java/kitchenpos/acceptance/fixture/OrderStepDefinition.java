package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.http.HttpStatus;

public class OrderStepDefinition {

    public static long 주문을_생성한다(
        final long orderTableId,
        final LocalDateTime orderedTime,
        final List<Long> orderLineItemIds,
        final long quantity) {

        List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
            .map(orderLineItemId -> new OrderLineItem(orderLineItemId, quantity))
            .collect(Collectors.toList());

        Order order = new Order(orderTableId, orderedTime, orderLineItems);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(order)
            .when().log().all()
            .post("/api/orders")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<Order> 주문을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/orders")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", Order.class);
    }

    public static void 주문의_상태를_변경한다(
        final long orderId,
        final String orderStatus) {

        Order order = new Order(null, orderStatus);

        RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(order)
            .when().log().all()
            .put("/api/orders/" + orderId + "/order-status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
