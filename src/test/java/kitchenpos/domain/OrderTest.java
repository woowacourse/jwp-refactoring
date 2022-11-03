package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("주문 도메인의")
class OrderTest {

    @Nested
    @DisplayName("정적 팩토리 메서드 ofCooking은")
    class OfCooking {

        @Test
        @DisplayName("주문 테이블이 빈 테이블일 수 없다.")
        void ofCooking_emptyOrderTable_exception() {
            // given
            final OrderTable orderTable = getEmptyOrderTable();

            // when & then
            assertThatThrownBy(() -> Order.ofCooking(orderTable, LocalDateTime.now(), Collections.emptyList()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private OrderTable getEmptyOrderTable() {
            return OrderTable.of(7, true);
        }
    }

    @Nested
    @DisplayName("changeOrderStatus은")
    class ChangeOrderStatus {

        @ParameterizedTest
        @DisplayName("주문이 이미 완료된 상태면 주문 상태를 변경할 수 없다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        void changeOrderStatus_orderStatusIsCompletion_exception(final String orderStatus) {
            // given
            final Order order = getCompletedOrder();

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Order getCompletedOrder() {
            return new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());
        }
    }
}
