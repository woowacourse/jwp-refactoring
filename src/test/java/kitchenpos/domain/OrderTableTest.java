package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @Test
    @DisplayName("empty 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("OrderTable 이 TableGroup 에 배정되어 있으면 empty 를 변경할 때 예외를 던진다.")
    void changeEmpty_grouped_exception() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1, true);
        orderTable.group(tableGroupId);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("could not change empty, table is now grouped");
    }

    @Test
    @DisplayName("OrderTable 을 TableGroup 에 배정한다.")
    void group() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1, true);

        // when
        orderTable.group(tableGroupId);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
        assertThat(orderTable.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("OrderTable 을 TableGroup 에서 제외한다.")
    void ungroup() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1, true);
        orderTable.group(tableGroupId);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isEqualTo(false);
    }
}
