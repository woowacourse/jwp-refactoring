package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Nested
    class newCookingInstanceOf_정적_팩토리_메소드는 {

        @Test
        void Cooking_상태의_Order_인스턴스를_반환한다() {
            // given
            OrderLineItem orderLineItem1 = new OrderLineItem(null, 1L, 2L);
            OrderLineItem orderLineItem2 = new OrderLineItem(null, 2L, 2L);

            // when
            Order order = Order.newCookingInstanceOf(1L, List.of(orderLineItem1, orderLineItem2), 2L);

            // & then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문_목록이_비어_있으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(
                    () -> Order.newCookingInstanceOf(1L, Collections.emptyList(), 2L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력_받은_값과_주문_목록의_사이즈가_다르다면_예외가_발생한다() {
            // given
            OrderLineItem orderLineItem1 = new OrderLineItem(null, 1L, 2L);
            OrderLineItem orderLineItem2 = new OrderLineItem(null, 2L, 2L);

            // when & then
            assertThatThrownBy(
                    () -> Order.newCookingInstanceOf(1L, List.of(orderLineItem1, orderLineItem2), 1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
