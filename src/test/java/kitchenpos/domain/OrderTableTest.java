package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("빈 상태이면 예외를 발생시킨다.")
    @Test
    void isEmpty_exception() {
        // then
        assertThatThrownBy(() -> new OrderTable(1L, 3, true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
