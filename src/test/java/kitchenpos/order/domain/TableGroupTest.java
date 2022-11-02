package kitchenpos.order.domain;

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
    @DisplayName("주문 테이블이 빈 경우 예외를 던진다.")
    void create_empty_table() {
        // given
        TableGroupValidator validator = new TableGroupValidator();

        // when, then
        assertThatThrownBy(() -> validator.validateOrderTables(List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블크기가 2 미만인 경우 예외를 던진다.")
    void create_table_under_size2() {
        // given
        OrderTable orderTable = new OrderTable(1, true);
        TableGroupValidator validator = new TableGroupValidator();

        // when, then
        assertThatThrownBy(() -> validator.validateOrderTables(List.of(orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹에서 주문 테이블 할당을 제거한다.")
    void ungroupOrderTables() {
        // given
        OrderTable orderTable1 = new OrderTable(2, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when
        tableGroup.ungroupOrderTables(List.of(orderTable1, orderTable2));

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.isEmpty()).isFalse();
        assertThat(orderTable2.isEmpty()).isFalse();
    }
}
