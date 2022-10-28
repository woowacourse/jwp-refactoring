package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성 시 주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderLineItem() {
        assertThatThrownBy(() -> new Order(1L, "COOKING", LocalDateTime.now(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문아이템 갯수를 확인한다.")
    @Test
    void hasValidSize() {
        Order order = new Order(1L, "COOKING", LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(), new OrderLineItem()));

        assertThat(order.hasValidSize(2L)).isTrue();
    }

    @DisplayName("주문의 주문 상태를 확인한다.")
    @Test
    void hasStatus() {
        Order order = new Order(1L, "COOKING", LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(), new OrderLineItem()));

        assertAll(
                () -> assertThat(order.hasStatus(OrderStatus.COOKING)).isTrue(),
                () -> assertThat(order.hasStatus(OrderStatus.MEAL)).isFalse()
        );
    }
}
