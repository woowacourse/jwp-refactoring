package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidOrderTableSizesException;
import kitchenpos.domain.exceptions.TableAlreadyHasGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @DisplayName("TableGroup 생성 실패 - 주문 테이블 2개 미만")
    @Test
    public void createFailTableGroup() {
        assertThatThrownBy(() -> OrderTables.of(
                Lists.newArrayList(11L),
                Lists.newArrayList(new OrderTable(11L, null, 4, false))
        ))
                .isInstanceOf(InvalidOrderTableSizesException.class);
    }

    @DisplayName("TableGroup 생성")
    @Test
    public void createTableGroup() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("이미 TableGroup 되어 있는 경우 grouping 불가")
    @Test
    public void validateTablesForGrouping_alreadyGrouping() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 4, true);
        OrderTable orderTable2 = new OrderTable(11L, tableGroup, 2, true);

        assertThatThrownBy(() -> OrderTables.validatedForGrouping(Lists.newArrayList(1L, 11L),
                Lists.newArrayList(orderTable1, orderTable2), tableGroup))
                .isInstanceOf(TableAlreadyHasGroupException.class);
    }

    @DisplayName("Table 상태가 false인 경우 grouping 불가")
    @Test
    public void validateTablesForGrouping_emptyFalse() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable(1L, null, 5, false);
        OrderTable orderTable2 = new OrderTable(11L, null, 6, false);

        assertThatThrownBy(() -> OrderTables.validatedForGrouping(Lists.newArrayList(1L, 11L),
                Lists.newArrayList(orderTable1, orderTable2), tableGroup))
                .isInstanceOf(TableAlreadyHasGroupException.class);
    }
}