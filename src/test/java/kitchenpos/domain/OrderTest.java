package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @Test
    @DisplayName("주문 상태가 완료 상태면 true를 반환한다.")
    void isCompletion() {
        final Order order = new Order(null, OrderStatus.COMPLETION.name(), null, null);

        final Boolean actual = order.isCompletion();

        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 완료 상태가 아니면 false를 반환한다.")
    void isCompletion_NotCompletion(final String orderStatus) {
        final Order order = new Order(null, orderStatus, null, null);

        final Boolean actual = order.isCompletion();

        assertThat(actual).isFalse();
    }
}
