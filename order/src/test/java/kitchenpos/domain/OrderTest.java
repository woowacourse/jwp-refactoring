package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.exception.OrderIsCompletedException;
import kitchenpos.exception.OrderIsNotCompletedException;
import kitchenpos.exception.OrderLineEmptyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_상태가_완료라면_상태를_변경할_때_예외를_던진다() {
        // given
        Order order = new Order(null, OrderStatus.COMPLETION, null);

        // when, then
        Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(OrderIsCompletedException.class);
    }

    @Test
    void 주문_상태가_완료가_아니라면_상태를_변경할_수_있다() {
        // given
        Order order = new Order(null, OrderStatus.COOKING, null);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_상태가_완료가_아니라면_예외를_던진다() {
        // given
        Order order = new Order(null, OrderStatus.COOKING, null);

        // when, then
        Assertions.assertThatThrownBy(() -> order.validateOrderIsCompleted())
                .isInstanceOf(OrderIsNotCompletedException.class);
    }

    @Test
    void 주문_상태가_완료라면_예외를_던지지_않는다() {
        // given
        Order order = new Order(null, OrderStatus.COMPLETION, null);

        // when, then
        Assertions.assertThatCode(() -> order.validateOrderIsCompleted())
                .doesNotThrowAnyException();
    }

    @Test
    void 주문_항목이_비었다면_예외를_던진다() {
        // given
        Order order = new Order(null, OrderStatus.COMPLETION, null);

        // when, then
        Assertions.assertThatThrownBy(() -> order.setupOrderLineItems(List.of()))
                .isInstanceOf(OrderLineEmptyException.class);
    }
}
