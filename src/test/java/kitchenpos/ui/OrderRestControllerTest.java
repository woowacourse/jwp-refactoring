package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
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
        Order requestBody = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        requestBody.setOrderTableId(8L);
        requestBody.setOrderLineItems(Arrays.asList(orderLineItem));

        // when, then
        webTestClient.post()
            .uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/orders/1")
            .expectBody(Order.class)
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
            .expectBody(new ParameterizedTypeReference<List<Order>>() {})
            .value(response -> assertThat(response).hasSize(1));
    }

    @DisplayName("changeOrderStatus 메서드는 Order의 상태를 변경시킨다.")
    @Test
    void changeOrderStatus_success() {
        // given
        create_order();
        Order requestBody = new Order();
        requestBody.setOrderStatus(OrderStatus.COMPLETION.name());

        // when, then
        webTestClient.put()
            .uri("/api/orders/{orderId}/order-status", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Order.class)
            .value(response -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()));
    }
}
