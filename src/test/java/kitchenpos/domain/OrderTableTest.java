package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("손님 수가 음수인 주문 테이블을 생성할 수 없다")
    @Test
    void create_numberOfGuestNegative() {
        assertThatThrownBy(() -> new OrderTable(-1, true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
