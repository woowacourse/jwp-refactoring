package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 테이블의_크기가_2보다_작으면_그룹_테이블을_생성할_수_없다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable = new OrderTable(tableGroup, 3, true);
        final List<OrderTable> orderTables = Arrays.asList(orderTable);

        assertThatThrownBy(
                () -> {
                    final OrderTables tables = new OrderTables(orderTables);
                    tables.validatePossibleBindTableGroup();
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_비어있지_않으면_그룹_테이블을_생성할_수_없다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable = new OrderTable(tableGroup, 3, true);
        OrderTable orderTable2 = new OrderTable(tableGroup, 3, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);

        assertThatThrownBy(
                () -> {
                    final OrderTables tables = new OrderTables(orderTables);
                    tables.validateMakeTableGroup(2);
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
