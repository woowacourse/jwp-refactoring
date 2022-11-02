package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.OrderTable;

class OrderTableTest {

    @Nested
    @DisplayName("group()")
    class GroupMethod {

        @Test
        @DisplayName("그룹을 할당한다.")
        void group() {
            // given
            OrderTable orderTable = new OrderTable(10, true);

            // when
            orderTable.group(1L);

            // then
            assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
            );
        }

        @Test
        @DisplayName("table group이 null이 아닌 경우 예외가 발생한다.")
        void alreadyGroup() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            orderTable.group(1L);

            // when, then
            assertThatThrownBy(() -> orderTable.group(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 다른 그룹에 존재하는 테이블입니다.");
        }

        @Test
        @DisplayName("비어있지 않은 경우 예외가 발생한다.")
        void notEmpty() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when, then
            assertThatThrownBy(() -> orderTable.group(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class UngroupMethod {

        @Test
        @DisplayName("그룹을 해제한다.")
        void ungroup() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            orderTable.group(1L);

            // when
            orderTable.ungroup();

            // then
            assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
            );
        }

    }

    @Nested
    @DisplayName("changeEmpty()")
    class ChangeEmptyMethod {

        @Test
        @DisplayName("empty 상태를 변경한다.")
        void changeEmpty() {
            // given
            OrderTable orderTable = new OrderTable(10, true);

            // when
            orderTable.changeEmpty(false);

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("table group이 null이 아닌 경우 예외가 발생한다.")
        void notNullTableGroupId() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            orderTable.group(1L);

            // when, then
            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 그룹에 묶여있어 상태를 변경할 수 없습니다.");
        }

    }

    @Nested
    @DisplayName("changeNumberOfGuests()")
    class ChangeNumberOfGuestsMethod {

        @Test
        @DisplayName("손님 수를 변경한다.")
        void changeNumberOfGuests() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when
            orderTable.changeNumberOfGuests(20);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(20);
        }

        @Test
        @DisplayName("0 미만의 수인 경우 예외가 발생한다.")
        void lessThanZero() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 0 이상이어야 합니다.");
        }

        @Test
        @DisplayName("비어있는 테이블인 경우 예외가 발생한다.")
        void emptyTable() {
            // given
            OrderTable orderTable = new OrderTable(10, true);

            // when, then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블입니다.");
        }

    }

}
