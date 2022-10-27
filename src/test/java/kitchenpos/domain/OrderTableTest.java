package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("그룹을 해제한다.")
    void ungroup() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(true);

        // when
        orderTable.ungroup();

        // then
        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isNull(),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

}
