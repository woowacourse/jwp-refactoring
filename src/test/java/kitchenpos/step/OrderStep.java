package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderUpdateRequest;

import java.util.List;

import static io.restassured.http.ContentType.JSON;

public class OrderStep {

    public static OrderCreateRequest ORDER_CREATE_REQUEST(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return new OrderCreateRequest(
                orderTableId,
                orderLineItems
        );
    }

    public static OrderUpdateRequest ORDER_UPDATE_REQUEST(final String orderStatus) {
        return new OrderUpdateRequest(orderStatus);
    }

    public static OrderCreateRequest toRequest(final Order order) {
        return new OrderCreateRequest(
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems()
        );
    }

    public static Order 주문_생성_요청하고_주문_반환(final OrderCreateRequest request) {
        final ExtractableResponse<Response> response = 주문_생성_요청(request);
        return response.jsonPath().getObject("", Order.class);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(final OrderCreateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .post("/api/orders")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .get("/api/orders")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final Long orderId, final OrderUpdateRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .put("/api/orders/" + orderId + "/order-status")

                .then()
                .log().all()
                .extract();
    }
}
