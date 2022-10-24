package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Test
    void create() {
        // given
        OrderTable orderTable1 = getOrderTable(1L, null, 3, true);
        OrderTable orderTable2 = getOrderTable(2L, null, 3, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        TableGroup tableGroup = getTableGroup(1L, orderTables);

        given(orderTableDao.findAllByIdIn(List.of(1L, 2L)))
                .willReturn(orderTables);
        given(tableGroupDao.save(any()))
                .willReturn(tableGroup);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).hasSize(orderTables.size())
        );
    }

    @Test
    void ungroup() {
        // given
        OrderTable orderTable1 = getOrderTable(1L, null, 3, true);
        OrderTable orderTable2 = getOrderTable(2L, null, 3, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        TableGroup tableGroup = getTableGroup(1L, orderTables);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(orderTables);

        // when
        tableGroupService.ungroup(1L);

        // then
        List<Long> tableGroupIds = orderTables.stream()
                .map(OrderTable::getTableGroupId)
                .collect(Collectors.toList());
        List<Boolean> empties = orderTables.stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(tableGroupIds).containsOnlyNulls(),
                () -> assertThat(empties).containsOnly(false)
        );
    }
}
