package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COMPLETION"} ,mode = EXCLUDE)
    void 주문상태가_완료가_아닌지_확인한다_맞는경우(OrderStatus orderStatus) {
        // given
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
        Order order = new Order(new OrderTable(1L, 5, false), orderStatus, orderLineItems);

        // when && then
        assertThat(order.isNotCompletion()).isTrue();
    }

    @Test
    void 주문상태가_완료가_아닌지_확인한다_틀린경우() {
        // given
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
        Order order = new Order(new OrderTable(1L, 5, false), OrderStatus.COMPLETION, orderLineItems);

        // when && then
        assertThat(order.isNotCompletion()).isFalse();
    }

    @Nested
    class 주문상태_변경시 {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"} ,mode = EXCLUDE)
        void 성공한다(OrderStatus orderStatus) {
            // given
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
            Order order = new Order(new OrderTable(1L, 5, false), orderStatus, orderLineItems);

            // when
            order.changeOrderStatus(orderStatus);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class)
        void 주문상태가_완료라면_변경할_수_없다(OrderStatus orderStatus) {
            // given
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
            Order order = new Order(new OrderTable(1L, 5, false), OrderStatus.COMPLETION, orderLineItems);

            // when && then
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 완료 상태면 변경할 수 없습니다.");
        }
    }
}
