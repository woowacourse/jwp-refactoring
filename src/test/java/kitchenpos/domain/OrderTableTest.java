package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 테이블이 비어 있다면 변경할 수 없다.")
    void changeNumberOfGuests_emptyTable() {
        // given
        final OrderTable 두명_테이블 = new OrderTable(2, true);

        // when & then
        assertThatThrownBy(() -> 두명_테이블.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 비어있음 정보를 변경할 때 테이블이 그룹화되어 있다면 변경할 수 없다.")
    void changeEmpty_groupedTable() {
        // given
        final OrderTable 두명_테이블 = new OrderTable(2, true);
        final OrderTable 세명_테이블 = new OrderTable(3, true);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.initOrderTables(List.of(두명_테이블, 세명_테이블));

        // when & then
        assertThatThrownBy(() -> 두명_테이블.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화된 테이블의 상태를 변경할 수 없습니다.");
    }
}
