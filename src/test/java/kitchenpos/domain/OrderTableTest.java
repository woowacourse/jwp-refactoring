package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {

    @DisplayName("주문 테이블 생성 시, 방문한 손님 수가 0명 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void orderTable_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // when & then
        assertThatThrownBy(() -> OrderTable.create(invalidNumberOfGuests, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void orderTable() {
        // then
        assertDoesNotThrow(() -> OrderTable.create(1, true));
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경 시, 빈 테이블이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithInvalidEmptyStatus() {
        // given
        OrderTable orderTable = OrderTable.create(1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블의 방문한 손님 수는 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경 시, 방문한 손님 수가 0명 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void changeNumberOfGuests_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.create(1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {2, 100})
    void changeNumberOfGuests(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.create(1, false);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
