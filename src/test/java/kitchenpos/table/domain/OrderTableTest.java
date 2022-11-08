package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    @DisplayName("그룹화 가능한 테이블인지 여부를 반환한다")
    void isGroupAble() {
        final OrderTable orderTable = new OrderTable(2, true);

        assertThat(orderTable.isGroupAble()).isTrue();
    }

    @Test
    @DisplayName("그룹 테이블로 변경한다")
    void changeGroupTable() {
        final OrderTable orderTable = new OrderTable(2, true);

        orderTable.changeGroupTable(1L);

        assertThat(orderTable.getTableGroupId()).isNotNull();
    }

    @Test
    @DisplayName("그룹 해제 테이블로 변경한다")
    void changeUngroupTable() {
        final OrderTable orderTable = new OrderTable(2, true);

        orderTable.changeUngroupTable();

        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
