package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @Test
    @DisplayName("인원수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when
        final OrderTable actual = orderTable.changeNumberOfGuests(6);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(6);
    }

    @Test
    @DisplayName("변경할 인원수가 0 미만이면 예외를 던진다.")
    void changeNumberOfGuests_underZero_throwException() {
        // given
        final OrderTable orderTable = new OrderTable(5, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블일 때 인원수 변경할 경우 예외를 던진다.")
    void changeNumberOfGuests_emptyTable_throwException() {
        // given
        final OrderTable orderTable = new OrderTable(5, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블을 빈 테이블로 변경할 수 있다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when
        final OrderTable actual = orderTable.changeEmpty(true);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("빈 테이블을 비지 않은 테이블로 변경할 수 있다.")
    void changeEmpty_reverse() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when
        final OrderTable actual = orderTable.changeEmpty(false);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("그룹에 속해 있을 때 빈 상태를 변경하려하면 예외를 던진다.")
    void changeEmpty_hasGroup_throwException() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1L, 3, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
