package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("OrderTable 클래스의")
class OrderTableTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuests_ExceptionThrown() {
            assertThatThrownBy(() -> new OrderTable(-1, false))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateEmpty 메서드는")
    class UpdateEmpty {

        @Test
        @DisplayName("주문 테이블의 empty 여부를 업데이트한다.")
        void success() {
            final OrderTable orderTable = new OrderTable(1L, null, 2, false);
            orderTable.updateEmpty(true);

            assertThat(orderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블이 단체로 지정된 테이블인 경우 empty 상태를 수정할 수 없다.")
        void hasTableGroup_ExceptionThrown() {
            final OrderTable orderTable = new OrderTable(1L, 1L, 2, false);

            assertThatThrownBy(() -> orderTable.updateEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 주문 상태가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown() {
            final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
            final Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));
            final OrderTable orderTable = new OrderTable(1L, null, 2, false, List.of(order));

            assertThatThrownBy(() -> orderTable.updateEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateNumberOfGuests 메서드는")
    class UpdateNumberOfGuests {

        @Test
        @DisplayName("테이블 인원 수를 업데이트한다.")
        void success() {
            final OrderTable orderTable = new OrderTable(1, false);
            orderTable.updateNumberOfGuests(2);
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuests_ExceptionThrown() {
            final OrderTable orderTable = new OrderTable(1, false);
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("빈 테이블인 경우 예외를 던진다.")
        void empty_ExceptionThrown() {
            final OrderTable orderTable = new OrderTable(0, true);
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        @ParameterizedTest
        @EnumSource(
                value = OrderStatus.class,
                names = {"COMPLETION"},
                mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown(OrderStatus orderStatus) {
            final Order order = new Order(1L, 2L, orderStatus.name(), LocalDateTime.now());
            OrderTable orderTable = new OrderTable(2L, 1L, 2, false, List.of(order));
            assertThatThrownBy(orderTable::ungroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
