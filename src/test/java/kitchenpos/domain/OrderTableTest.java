package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 비움상태를_변경할때_단체지정되어있으면_예외가_발생한다() {
        OrderTable orderTable = new OrderTable(1L, 1L, 3, true);
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 테이블 상태를 변화할 수 없습니다.");
    }
}
