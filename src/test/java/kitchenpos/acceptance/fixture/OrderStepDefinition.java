package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderUpdateRequest;
import org.springframework.http.HttpStatus;

public class OrderStepDefinition {

    public static long 주문을_생성한다(
        final long orderTableId,
        final List<Long> orderLineItemIds,
        final long quantity) {

        List<OrderLineItemRequest> orderLineItems = orderLineItemIds.stream()
            .map(orderLineItemId -> new OrderLineItemRequest(orderLineItemId, quantity))
            .collect(Collectors.toList());

        OrderCreateRequest order = new OrderCreateRequest(orderTableId, orderLineItems);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(order)
            .when().log().all()
            .post("/api/orders")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<OrderResponse> 주문을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/orders")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", OrderResponse.class);
    }

    public static void 주문의_상태를_변경한다(
        final long orderId,
        final String orderStatus) {

        OrderUpdateRequest order = new OrderUpdateRequest(orderStatus);

        RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(order)
            .when().log().all()
            .put("/api/orders/" + orderId + "/order-status")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
