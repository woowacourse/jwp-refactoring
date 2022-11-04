package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 테이블이_없는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> TableGroup.of(null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    void 테이블의_수가_2개_이하인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> TableGroup.of(List.of(OrderTable.of(0, true))))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이여야 합니다.");
    }
}
