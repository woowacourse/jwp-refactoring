package kitchenpos.ui;

import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupRestControllerTest extends ControllerTest {

    private OrderTable table1;
    private OrderTable table2;

    @BeforeEach
    void setUp() throws Exception {
        // given
        OrderTable table = table();
        OrderTable other = table();
        table.setEmpty(true);
        other.setEmpty(true);
        table1 = changeEmpty(table);
        table2 = changeEmpty(other);
    }

    @Test
    void create() throws Exception {
        // when
        TableGroup tableGroup = tableGroup(Arrays.asList(table1, table2));
        List<OrderTable> tables = tableGroup.getOrderTables();

        // then
        assertAll(
                () -> assertThat(tableGroup.getId()).isEqualTo(1L),
                () -> assertThat(tableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(tables.get(0).getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(tables.get(0).isEmpty()).isFalse()
        );
    }

    @Test
    void create_insufficient() throws Exception {
        // when
        assertThatThrownBy(() -> tableGroup(Collections.singletonList(table1)))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void create_nonExisting() throws Exception {
        // given
        OrderTable nonExistingTable = new OrderTable();
        nonExistingTable.setId(3L);

        // when
        assertThatThrownBy(() -> tableGroup(Arrays.asList(table1, table2, nonExistingTable)))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() throws Exception {
        // given
        TableGroup tableGroup = tableGroup(Arrays.asList(table1, table2));

        // when
        ungroup(tableGroup);
        List<OrderTable> tables = tables();
        OrderTable tableUngrouped = tables.stream()
                .filter(table -> table.getId().equals(table1.getId()))
                .findFirst().get();

        // then
        assertAll(
                () -> assertThat(tableUngrouped.getTableGroupId()).isNull(),
                () -> assertThat(tableUngrouped.isEmpty()).isFalse()
        );
    }

    @Test
    void ungroup_invalidOrderStatus() throws Exception {
        // given
        TableGroup tableGroup = tableGroup(Arrays.asList(table1, table2));
        OrderLineItem orderLineItem = orderLineItem();
        Order order = order(Collections.singletonList(orderLineItem), table1);
        order.setOrderStatus(OrderStatus.COOKING.name());
        changeStatus(order);

        // when
        assertThatThrownBy(() -> ungroup(tableGroup))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}