package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 인수 테스트")
public class OrdersAcceptanceTest extends DomainAcceptanceTest {
    @DisplayName("POST /api/orders")
    @Test
    void create() {
        // given
        // menu 등록
        Long menuId = POST_SAMPLE_MENU();

        // orderTable 등록
        long orderTableId = POST_SAMPLE_ORDER_TABLE(1, false);
        OrderRequest orderRequest = OrderRequest.of(
                orderTableId,
                singletonList(OrderItemRequest.of(menuId, 1L))
        );

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/orders")
    @Test
    void list() {
        // given
        POST_SAMPLE_ORDER();

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("PUT /api/orders/{orderId}/order-status")
    @Test
    void changeOrderStatus() {
        // given
        long orderId = POST_SAMPLE_ORDER();
        OrderStatusRequest orderStatusRequest
                = OrderStatusRequest.from("COMPLETION");

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderStatusRequest)
                .when().put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        OrderStatusResponse orderStatusResponse = response.as(OrderStatusResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderStatusResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
