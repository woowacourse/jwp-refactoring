package kitchenpos.integration;

import kitchenpos.domain.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderIntegrationTest extends IntegrationTest {

    @Test
    void 주문_생성을_요청한다() {
        // given
        final Order order = new Order();
        final HttpEntity<Order> request = new HttpEntity<>(order);

        // when
        final ResponseEntity<Order> response = testRestTemplate
                .postForEntity("api/orders", request, Order.class);
        final Order createdOrder = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/orders/" + createdOrder.getId()),


        );
    }
}
