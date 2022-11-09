package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);

        // when, then
        assertDoesNotThrow(() -> new TableGroup(List.of(orderTable1, orderTable2)));
    }

    @Test
    @DisplayName("테이블 그룹에서 주문 테이블 할당을 제거한다.")
    void ungroupOrderTables() {
        // given
        OrderTable orderTable1 = new OrderTable(2, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when
        tableGroup.ungroupOrderTables(it -> {});

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.isEmpty()).isFalse();
        assertThat(orderTable2.isEmpty()).isFalse();
    }
}
