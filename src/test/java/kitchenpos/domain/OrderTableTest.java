package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문이 불가능하다")
    void validateOrderable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        assertThatThrownBy(orderTable::validateOrderable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
