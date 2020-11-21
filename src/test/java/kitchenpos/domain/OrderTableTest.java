package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @DisplayName("OrderTable의 empty가 true일 때")
    @Test
    void isEmpty_whenEmptyIsTrue() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

        assertThat(orderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("OrderTable의 empty가 false일 때")
    @Test
    void isEmpty_whenEmptyIsFalse() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        assertThat(orderTable.isEmpty()).isEqualTo(false);
    }
}