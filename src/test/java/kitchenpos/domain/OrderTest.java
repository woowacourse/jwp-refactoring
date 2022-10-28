package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Nested
    class create_정적_팩토리_메소드는 {

        @Test
        void 주문_목록이_비어_있으면_예외가_발생한다() {
            // when & then
            Assertions.assertThatThrownBy(
                            () -> Order.create(1L, OrderStatus.COOKING.name(), Collections.emptyList(), 2L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력_받은_값과_주문_목록의_사이즈가_다르다면_예외가_발생한다() {
            // given
            OrderLineItem orderLineItem1 = new OrderLineItem(null, 1L, 2L);
            OrderLineItem orderLineItem2 = new OrderLineItem(null, 2L, 2L);

            // when & then
            Assertions.assertThatThrownBy(
                            () -> Order.create(1L, OrderStatus.COOKING.name(), List.of(orderLineItem1, orderLineItem2), 1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
