package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.List;
import kitchenpos.domain.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문(Order) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    private final OrderValidator validator = mock(OrderValidator.class);

    @Nested
    class 상태_변경_시 {

        @Test
        void 결제되지_않은_주문의_상태를_변경한다() {
            // given
            Order order = new Order(1L, List.of(), validator);

            // when
            order.setOrderStatus(COMPLETION.name());

            // then
            assertThat(order.getOrderStatus()).isEqualTo(COMPLETION.name());
        }

        @Test
        void 이미_결제_완료된_주문은_상태를_변경할_수_없다() {
            // given
            Order order = new Order(1L, List.of(), validator);
            order.setOrderStatus(COMPLETION.name());

            // when & then
            assertThatThrownBy(() ->
                    order.setOrderStatus(OrderStatus.MEAL.name())
            ).isInstanceOf(OrderException.class)
                    .hasMessage("이미 결제 완료된 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
