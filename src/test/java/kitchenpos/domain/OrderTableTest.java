package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("인원수를 음수로 설정하는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        OrderTable orderTable = new OrderTable(100, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 Empty인 경우 인원수 설정 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        OrderTable orderTable = new OrderTable(100, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(50))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
