package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;
import static kitchenpos.acceptance.TableAcceptanceTest.POST_DEFAULT_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    @DisplayName("POST /api/orders")
    @Test
    void create() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        // orderTable 등록
        long orderTableId = POST_DEFAULT_ORDER_TABLE();

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTableId);
        params.put("orderLineItems", singletonList(orderLineItem));

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
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
        long orderId = POST_DEFAULT_ORDER();
        Map<String, Object> params = new HashMap<>();
        params.put("orderStatus", "COMPLETION");

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/orders/" + orderId + "/order-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Order order = response.as(Order.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    public static long POST_DEFAULT_ORDER() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        long orderTableId = POST_DEFAULT_ORDER_TABLE();

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTableId);
        params.put("orderLineItems", singletonList(orderLineItem));

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        Order order = response.as(Order.class);
        return order.getId();
    }
}
