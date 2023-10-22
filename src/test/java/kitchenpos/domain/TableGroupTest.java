package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupTest {

    private final OrderTable orderTable1 = OrderTable.create(1, true);
    private final OrderTable orderTable2 = OrderTable.create(1, true);

    @DisplayName("테이블 그룹 생성 시, 주문 테이블의 크기가 2개 미만이면 예외가 발생한다.")
    @Test
    void tableGroup_FailWithInvalidOrderTableSize() {
        // given
        List<OrderTable> invalidOrderTable = List.of(orderTable1);

        // when & then
        assertThatThrownBy(() -> TableGroup.createWithGrouping(invalidOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다.");
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void tableGroup() {
        // then
        assertDoesNotThrow(() -> TableGroup.createWithGrouping(List.of(orderTable1, orderTable2)));
    }
}
