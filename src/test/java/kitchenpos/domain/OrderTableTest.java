package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("빈 상태로 생성하면 예외를 발생시킨다.")
    @Test
    void isEmpty_exception() {
        // then
        assertThatThrownBy(() -> new OrderTable(1L, 3, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 상태로 바꾼다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        // when
        orderTable.changeEmpty();

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("손님 수를 바꾼다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        // when
        orderTable.changeNumberOfGuests(2);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("손님 수가 0보다 작으면 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuestsLessThanZero_exception() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 포함되어 있는지 알 수 있다.")
    @Test
    void isTableGrouping() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        // when
        boolean actual = orderTable.isTableGrouping();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void upgroup() {
        // given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        // when
        orderTable.upgroup();

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
}
