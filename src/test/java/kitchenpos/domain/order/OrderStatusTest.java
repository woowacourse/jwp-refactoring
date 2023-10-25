package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.OrderException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderStatusTest {

    @Test
    void 조리_중_상태는_다른_상태로_변경할_수_있다() {
        // given
        final OrderStatus orderStatus = COOKING;

        // when
        final OrderStatus result = orderStatus.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(result).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 식사_중_상태는_다른_상태로_변경할_수_있다() {
        // given
        final OrderStatus orderStatus = OrderStatus.MEAL;

        // when
        final OrderStatus result = orderStatus.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(result).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 완료_상태는_다른_상태로_변경할_수_없다() {
        // given
        final OrderStatus orderStatus = OrderStatus.COMPLETION;

        // expected
        assertThatThrownBy(() -> orderStatus.changeOrderStatus(COOKING))
                .isInstanceOf(OrderException.CompletionOrderChangeStatusException.class);
    }
}
