package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 주문_테이블_생성() {
        Assertions.assertDoesNotThrow(() -> new OrderTable(1, true));
    }

    @Test
    void 손님의_수가_음수인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new OrderTable(-1, true));
    }
}
