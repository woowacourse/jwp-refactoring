package kitchenpos.domain;

import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.tablegroup.TableGroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class OrderTablesTest {

    @DisplayName("모든 테이블이 빈 테이블이고, 그룹 할당이 되지 않았다면 그룹 할당이 가능하다.")
    @Test
    void assignGroup_success() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        final TableGroup tableGroup = new TableGroup();
        orderTables.assignGroup(tableGroup);

        // then
        final List<Boolean> orderTablesEmptyStatus = orderTables.getOrderTables().stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toUnmodifiableList());
        assertThat(orderTablesEmptyStatus).containsOnly(false);

        final List<TableGroup> orderTablesGroup = orderTables.getOrderTables().stream()
                .map(OrderTable::getTableGroup)
                .collect(Collectors.toUnmodifiableList());
        assertThat(orderTablesGroup).containsOnly(tableGroup);
    }

    @DisplayName("하나의 테이블이 착석된 상태라면 테이블 그룹화에 실패한다.")
    @Test
    void assignGroup_fail_when_one_table_status_is_occupied() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        final TableGroup tableGroup = new TableGroup();

        // then
        assertThatCode(() -> orderTables.assignGroup(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 해제에 성공한다.")
    @Test
    void ungroup_success() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        orderTables.ungroup();

        // then
        final boolean actual = orderTables.getOrderTables().stream()
                .filter(OrderTable::isEmpty)
                .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroup()));
        assertThat(actual).isTrue();
    }

    @DisplayName("일급 컬렉션의 크기 검증에 성공한다. (실패 케이스)")
    @Test
    void validateSize_success_when_size_is_different() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        final int invalidSize = 3;

        // then
        assertThatThrownBy(() -> orderTables.validateSize(invalidSize))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("일급 컬렉션의 크기 검증에 성공한다. (성공 케이스)")
    @Test
    void validateSize_success_when_size_is_same() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        final int size = 2;

        // then
        assertThatCode(() -> orderTables.validateSize(size))
                .doesNotThrowAnyException();
    }
}
