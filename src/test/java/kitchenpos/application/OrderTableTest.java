package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {
    @DisplayName("0명 미만의 손님은 불가능하다.")
    @Test
    void invalidNumberOfGuestsTest() {
        assertThatThrownBy(() -> new OrderTable(-1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이면 손님의 인원 수를 수정할 수 없다.")
    @Test
    void updateNumberOfGuestsTest() {
        // given
        OrderTable orderTable = new OrderTable(10, true);

        // when, then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(20))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
