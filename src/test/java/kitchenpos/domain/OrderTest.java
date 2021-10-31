package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @DisplayName("주문을 생성한다. - 실패, 주문 테이블이 비어있음")
    @Test
    void createFailed() {
        // given - when
        OrderTable orderTable = new OrderTable(0, true);
        assertThatThrownBy(() -> new Order(orderTable))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("주문 테이블이 유효하지 않습니다.");
    }

    @DisplayName("주문이 완료되지 않은 상태인지 확인한다. - 완료된 경우 False를 리턴한다.")
    @Test
    void isNotCompleted() {
        // given
        Order order = new Order(new OrderTable(10, false), OrderStatus.COMPLETION.name());
        // when - then
        assertThat(order.isNotCompleted()).isFalse();
    }

    @DisplayName("주문이 완료되지 않은 상태인지 확인한다. - ")
    @ParameterizedTest(name = "{displayName} {0}인 경우 True를 리턴한다.")
    @ValueSource(strings = {"MEAL", "COOKING"})
    void isNotCompleted(String orderStatus) {
        // given
        Order order = new Order(new OrderTable(10, false),orderStatus);
        // when - then
        assertThat(order.isNotCompleted()).isTrue();
    }

    @DisplayName("주문 상태를 수정한다. - 실패, 이미 완료된 상태")
    @Test
    void updateOrderStatus() {
        // given
        Order order = new Order(new OrderTable(10, false), OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.COOKING.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 이미 완료된 상태입니다.");
    }
}
