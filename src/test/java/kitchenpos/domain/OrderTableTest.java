package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @DisplayName("주문 생성시 방문한 손님 수가 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void createWithInvalidNumberOfGuests(int numberOfGuests) {
        assertThatThrownBy(() -> new OrderTable(1L, numberOfGuests, false))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("방문한 손님 수 변경시 변경하려는 값이 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void updateWithInvalidNumberOfGuests(int numberOfGuests) {
        OrderTable orderTable = new OrderTable(1L, 10, false);
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("방문한 손님 수 변경시 빈 테이블이면 예외가 발생한다.")
    @Test
    void updateWithEmpty() {
        OrderTable orderTable = new OrderTable(1L, 10, true);
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(2))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("빈 테이블입니다.");
    }
}
