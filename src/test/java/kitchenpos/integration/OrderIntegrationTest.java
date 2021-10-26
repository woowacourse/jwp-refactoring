package kitchenpos.integration;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class OrderIntegrationTest extends IntegrationTest {

    @DisplayName("주문 목록을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/orders")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Order.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    public void create() {
        OrderTable orderTable = fixtureMaker.createOrderTableForNotEmpty();
        List<OrderLineItem> orderLineItems = fixtureMaker.createOrderLineItems();
        Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
        webTestClient.post().uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(order), Order.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Order.class);
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    public void changeOrderStatus() {
        Order order = fixtureMaker.createOrder();
        order.setOrderStatus(OrderStatus.COOKING.name());
        webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .path("/api/orders/{orderId}/order-status")
                .build(order.getId())
            )
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(order), Order.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TableGroup.class);
    }
}