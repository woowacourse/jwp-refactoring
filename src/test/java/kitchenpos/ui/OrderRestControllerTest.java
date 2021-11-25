package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.request.order.OrderChangeRequest;
import kitchenpos.ui.dto.request.order.OrderCreateRequest;
import kitchenpos.ui.dto.request.order.OrderLineItemRequest;
import kitchenpos.ui.dto.response.order.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("OrderRestController 통합 테스트")
class OrderRestControllerTest extends IntegrationTest {

    @DisplayName("create 메서드는 Order를 생성한다.")
    @Test
    void create_order() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(new OrderLineItemRequest(1L, 8L));
        OrderCreateRequest request = new OrderCreateRequest(8L, orderLineItemRequests);

        // when, then
        webTestClient.post()
            .uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/orders/1")
            .expectBody(OrderResponse.class)
            .value(response ->
                assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
            );
    }

    @DisplayName("list 메서드는 Order 목록을 조회한다.")
    @Test
    void list_orders() {
        // given
        create_order();

        // when, then
        webTestClient.get()
            .uri("/api/orders")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<List<OrderResponse>>() {})
            .value(response -> assertThat(response).hasSize(1));
    }

    @DisplayName("changeOrderStatus 메서드는 Order의 상태를 변경시킨다.")
    @Test
    void changeOrderStatus_success() {
        // given
        create_order();
        OrderChangeRequest request = new OrderChangeRequest(OrderStatus.COMPLETION.name());

        // when, then
        webTestClient.put()
            .uri("/api/orders/{orderId}/order-status", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(OrderResponse.class)
            .value(response -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()));
    }

    @DisplayName("changeOrderStatus 메서드는 현재 Order가 COMPLETION이면 상태 변경이 불가능하다.")
    @Test
    void changeOrderStatus_failure() {
        // given
        changeOrderStatus_success();
        OrderChangeRequest request = new OrderChangeRequest(OrderStatus.COOKING.name());

        // when, then
        webTestClient.put()
            .uri("/api/orders/{orderId}/order-status", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response -> assertThat(response).isEqualTo("Order 상태를 변경할 수 없는 상황입니다."));
    }
}
