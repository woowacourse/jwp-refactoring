package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        // when, then
        assertThatThrownBy(() -> new TableGroup(List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블크기가 2 미만인 경우 예외를 던진다.")
    void create_table_under_size2() {
        // given
        OrderTable orderTable = new OrderTable(1, true);

        // when, then
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
