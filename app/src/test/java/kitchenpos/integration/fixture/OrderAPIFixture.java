package kitchenpos.integration.fixture;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import org.springframework.http.MediaType;
import java.util.List;

public class OrderAPIFixture {

    public static ExtractableResponse<Response> createOrder(final OrderCreateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/orders")
                .then()
                .log().all()
                .extract();
    }

    public static OrderResponse createOrderAndReturnResponse(final OrderCreateRequest request) {
        return createOrder(request).as(OrderResponse.class);
    }

    public static List<OrderResponse> listOrder() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/orders")
                .then()
                .log().all()
                .extract();
        return response.as(new TypeRef<>() {
        });
    }

    public static OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest orderStatusChangeRequest) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(orderStatusChangeRequest)
                .put("/api/orders/{orderId}/order-status", orderId)
                .then()
                .log().all()
                .extract();
        return response.as(OrderResponse.class);
    }
}
