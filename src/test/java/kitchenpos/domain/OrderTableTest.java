package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @DisplayName("테이블 고객 수는 0 이상이어야 한다.")
    @Test
    void create_invalidCustomerCount() {
        int 음수_고객수 = -1;

        assertThatThrownBy(() -> new OrderTable(1L, null, 음수_고객수, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 고객 수는 0 이상이어야 한다.");
    }
}
