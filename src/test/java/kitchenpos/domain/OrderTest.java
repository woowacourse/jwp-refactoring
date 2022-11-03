package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @DisplayName("OrderLineItem이 비어있는 경우 예외를 발생한다.")
    @Test
    void orderLineItemThrowException() {
        assertThatThrownBy(
                () -> new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");
    }

    @DisplayName("Order create시 메뉴의 수와 다르면 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    void differentMenuSizeThrowException(int menuSize) {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, 1),
                new OrderLineItem(2L, 1)
        );
        assertThatThrownBy(
                () -> Order.create(1L, orderLineItems, menuSize)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 수가 부족합니다.");
    }

    @DisplayName("OrderStatus 변경 시 이미 완료된 주문일 경우 예외를 발생한다.")
    @Test
    void changeOrderStatusThrowException() {
        // given
        Order order = new Order(1L, OrderStatus.COMPLETION.name(),
                LocalDateTime.now(), Collections.singletonList(new OrderLineItem(1L, 1)
        ));

        // when & then
        assertThatThrownBy(
                () -> order.changeOrderStatus(OrderStatus.MEAL.name())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }
}
