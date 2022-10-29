package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    private OrderTable orderTable1 = new OrderTable(null, null, 3, true);
    private OrderTable orderTable2 = new OrderTable(null, null, 5, true);

    @Test
    void order_tables를_생성할_수_있다() {
        OrderTables orderTables = OrderTables.fromCreate(Arrays.asList(orderTable1, orderTable2));
        assertThat(orderTables.getValues()).hasSize(2);
    }

    @Test
    void order_table_size가_비어있으면_예외를_반환한다() {
        assertThatThrownBy(() -> OrderTables.fromCreate(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_table_size가_2보다_적으면_예외를_반환한다() {
        assertThatThrownBy(() -> OrderTables.fromCreate(Arrays.asList(orderTable1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_table이_비어있지_않으면_예외를_반환한다() {
        OrderTable notEmptyOrderTable = new OrderTable(null, null, 3, false);
        assertThatThrownBy(() -> OrderTables.fromCreate(Arrays.asList(orderTable1, notEmptyOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void table_group_id가_null이_아니면_예외를_반환한다() {
        Long tableGroupId = 1L;
        OrderTable notEmptyOrderTable = new OrderTable(null, tableGroupId, 3, true);
        assertThatThrownBy(() -> OrderTables.fromCreate(Arrays.asList(orderTable1, notEmptyOrderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void table_group_id를_채울_수_있다() {
        OrderTables orderTables = OrderTables.fromCreate(Arrays.asList(orderTable1, orderTable2));
        orderTables.fillTableGroup(new TableGroup(1L, LocalDateTime.now()));

        assertThat(orderTables.getValues()).hasSize(2)
                .extracting("tableGroupId")
                .isEqualTo(Arrays.asList(1L, 1L));
    }

    @Test
    void upgroup_할_수_있다() {
        OrderTables orderTables = OrderTables.fromCreate(Arrays.asList(orderTable1, orderTable2));
        orderTables.upgroup();

        assertThat(orderTables.getValues()).extracting("tableGroupId", "empty")
                .isEqualTo(Arrays.asList(tuple(null, false), tuple(null, false)));
    }
}
