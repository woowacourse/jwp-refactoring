package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@IgnoreDisplayNameUnderscores
class OrderTest {

    @Nested
    class newCookingInstanceOf_정적_팩토리_메소드는 {

        @Test
        void Cooking_상태의_Order_인스턴스를_반환한다() {
            // given
            OrderLineItem orderLineItem1 = new OrderLineItem(null, "메뉴 1", BigDecimal.valueOf(1000), 2L);
            OrderLineItem orderLineItem2 = new OrderLineItem(null, "메뉴 2", BigDecimal.valueOf(2000), 2L);

            // when
            Order order = Order.newCookingInstanceOf(1L, List.of(orderLineItem1, orderLineItem2));

            // & then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문_목록이_비어_있으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(
                    () -> Order.newCookingInstanceOf(1L, Collections.emptyList()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
