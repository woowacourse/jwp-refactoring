package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupTest {

    private final OrderTable orderTable1 = new OrderTable(1, true);
    private final OrderTable orderTable2 = new OrderTable(1, true);

    @DisplayName("테이블 그룹 생성 시, 주문 테이블의 크기가 2개 미만이면 예외가 발생한다.")
    @Test
    void tableGroup_FailWithInvalidOrderTableSize() {
        // given
        List<OrderTable> invalidOrderTable = List.of(orderTable1);

        // when & then
        assertThatThrownBy(() -> new TableGroup(invalidOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void tableGroup() {
        // then
        assertDoesNotThrow(() -> new TableGroup(List.of(orderTable1, orderTable2)));
    }
}
