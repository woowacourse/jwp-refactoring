package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableNumberOfGuests;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {
    @Nested
    class 주문의_상태를_변경한다 {
        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        void 주문의_상태를_변경한다(final String status) {
            final OrderStatus orderStatus = OrderStatus.valueOf(status);
            final OrderTable orderTable = new OrderTable(1L, new OrderTableNumberOfGuests(5), true);
            final Order order = new Order(orderTable.getId(), OrderStatus.MEAL, LocalDateTime.now());

            order.changeOrderStatus(orderStatus);

            assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        void 주문의_상태가_계산_완료일때_예외가_발생한다(final String status) {
            final OrderStatus orderStatus = OrderStatus.valueOf(status);
            final OrderTable orderTable = new OrderTable(1L, new OrderTableNumberOfGuests(5), true);
            final Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now());

            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
