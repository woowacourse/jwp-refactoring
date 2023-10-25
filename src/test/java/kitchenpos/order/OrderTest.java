package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.OrderTable;
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
    void 주문상태가_완료가_아닌지_확인한다_true(OrderStatus orderStatus) {
        // given
        Order order = getOrder(orderStatus);

        // when && then
        assertThat(order.isNotCompletion()).isTrue();
    }

    @Test
    void 주문상태가_완료가_아닌지_확인한다_false() {
        // given
        Order order = getOrder(OrderStatus.COMPLETION);

        // when && then
        assertThat(order.isNotCompletion()).isFalse();
    }

    @Nested
    class 주문상태_변경시 {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"} ,mode = EXCLUDE)
        void 성공(OrderStatus orderStatus) {
            // given
            Order order = getOrder(orderStatus);

            // when
            order.changeOrderStatus(orderStatus);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class)
        void 주문상태가_완료라면_예외(OrderStatus orderStatus) {
            // given
            Order order = getOrder(OrderStatus.COMPLETION);

            // when && then
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 완료 상태면 변경할 수 없습니다.");
        }
    }

    private Order getOrder(OrderStatus orderStatus) {
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
        return new Order(new OrderTable(5, false), orderStatus, orderLineItems, LocalDateTime.now());
    }
}
