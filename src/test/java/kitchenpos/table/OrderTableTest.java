package kitchenpos.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 테이블이_비어있으면_손님_수를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(3, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void 손님_수를_바꿀_때_양수이어야_한다(int numberOfGuests) {
        OrderTable orderTable = new OrderTable(3, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
