package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("이미 단체 지정된 테이블을 포함해서 단체 지정할 수 없다")
    void setId() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        TableGroup tableGroup = new TableGroup();
        orderTable.setTableGroup(tableGroup);

        // when, then
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다")
    void ungroup() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        TableGroup tableGroup = new TableGroup(new ArrayList<>(List.of(orderTable)));

        // when
        tableGroup.ungroup();

        // then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).isEmpty(),
            () -> assertThat(orderTable.getTableGroup()).isNull()
        );
    }
}
