package kitchenpos.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderUpdateOrderStatusRequest;
import kitchenpos.ui.response.OrderResponse;

import static io.restassured.http.ContentType.JSON;

public class OrderStep {

    public static OrderResponse 주문_생성_요청하고_주문_반환(final OrderCreateRequest request) {
        final ExtractableResponse<Response> response = 주문_생성_요청(request);
        return response.jsonPath().getObject("", OrderResponse.class);
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

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final Long orderId, final OrderUpdateOrderStatusRequest request) {
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
