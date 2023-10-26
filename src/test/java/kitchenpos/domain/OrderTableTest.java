package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 정상적으로 생성한다.")
    void createOrderTable() {
        // when
        final OrderTable orderTable = new OrderTable(null, 2, true);

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Nested
    @DisplayName("테이블의 상태를 변경할 때")
    class ChangeEmpty {

        @Test
        @DisplayName("정상적으로 변경된다.")
        void success() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, true);

            // when
            orderTable.changeEmpty(false);

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("테이블 그룹이 비어있지 않은 경우 예외가 발생한다.")
        void throwsExceptionWhenTableGroupIsNonNull() {
            // given
            final TableGroup tableGroup = new TableGroup(new ArrayList<>());
            final OrderTable orderTable = new OrderTable(tableGroup, 2, true);

            // when, then
            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 그룹은 비어있어야 합니다.");
        }
    }

    @Nested
    @DisplayName("방문한 손님 수를 변경할 때")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경된다.")
        void success() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);

            // when
            orderTable.changeNumberOfGuests(3);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        @DisplayName("방문한 손님 수가 음수인 경우 예외가 발생한다.")
        void throwsExceptionWhenNumberOfGuestsIsUnderZero() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIsEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, true);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    @Test
    @DisplayName("주문 테이블을 그룹해제한다.")
    void ungroup() {
        // given
        final TableGroup tableGroup = new TableGroup(new ArrayList<>());
        final OrderTable orderTable = new OrderTable(tableGroup, 2, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }
}
