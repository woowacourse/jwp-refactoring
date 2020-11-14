package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("0명 이하로 손님으로 테이블을 갱신할 수 없다.")
    @Test
    void createOrderTable_notValidNumber() {
        assertThatThrownBy(() -> new OrderTable(-1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("0명 이하로 손님을 갱신할 수 없다.")
    @Test
    void updateNumberOfGuest_notValidNumber() {
        OrderTable orderTable = new OrderTable(10, false);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 때 손님의 수를 갱신할 수 없다.")
    @Test
    void updateNumberOfGuest_notEmpty() {
        OrderTable orderTable = new OrderTable(10, true);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(11))
                .isInstanceOf(IllegalArgumentException.class);
    }
}