package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidOrderTableSizesException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @DisplayName("TableGroup 생성 실패 - 주문 테이블 2개 미만")
    @Test
    public void createFailTableGroup() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .isInstanceOf(InvalidOrderTableSizesException.class);
    }

    @DisplayName("TableGroup 생성")
    @Test
    public void createTableGroup() {
        OrderTable orderTable1 = new OrderTable(null, 4, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Lists.newArrayList(orderTable1, orderTable2));

        assertThat(tableGroup).isNotNull();
        assertThat(tableGroup.getOrderTables()).contains(orderTable1, orderTable2);
    }

    @DisplayName("TableGroup에 OrderTables 세팅")
    @Test
    public void setOrderTables() {
        OrderTable orderTable1 = new OrderTable(null, 4, true);
        OrderTable orderTable2 = new OrderTable(null, 2, true);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Lists.newArrayList(orderTable1, orderTable2));

        OrderTable orderTable3 = new OrderTable(null, 5, true);
        OrderTable orderTable4 = new OrderTable(null, 6, true);
        tableGroup.addOrderTables(Lists.newArrayList(orderTable3, orderTable4));

        assertThat(tableGroup).isNotNull();
        assertThat(tableGroup.getOrderTables()).contains(orderTable3, orderTable4);
    }

}