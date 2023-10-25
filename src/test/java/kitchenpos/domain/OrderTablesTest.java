package kitchenpos.domain;

import kitchenpos.ordertable.vo.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class OrderTablesTest {

    @DisplayName("모든 테이블이 그루핑할 수 있는 상태라면 모든 테이블의 상태를 occupied로 바꾸는데 성공한다.")
    @Test
    void validateCanGroupAndChangeToOccupied_success() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        orderTables.validateCanGroupAndChangeToOccupied();

        // then
        final List<Boolean> orderTablesEmptyStatus = orderTables.getOrderTables().stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toUnmodifiableList());
        assertThat(orderTablesEmptyStatus).containsOnly(false);
    }

    @DisplayName("하나의 테이블이 착석된 상태라면 모든 테이블의 상태를 occupied로 바꾸는데 실패한다.")
    @Test
    void validateCanGroupAndChangeToOccupied_fail_when_one_table_status_is_occupied() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // then
        assertThatCode(orderTables::validateCanGroupAndChangeToOccupied)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("매개변수로 주어진 사이즈와 일급 컬렉션의 크기가 일치하다면 그룹 해제에 성공한다.")
    @Test
    void validateSizeAndUngroup_success() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        orderTables.validateSizeAndUngroup(2);

        // then
        final boolean actual = orderTables.getOrderTables().stream()
                .filter(OrderTable::isEmpty)
                .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroup()));
        assertThat(actual).isTrue();
    }

    @DisplayName("매개변수로 주어진 사이즈와 일급 컬렉션의 크기가 다르면 그룹 해제에 실패한다.")
    @Test
    void validateSizeAndUngroup_fail_when_size_is_different() {
        // given
        final OrderTable orderTable1 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(1), true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        final int invalidSize = 3;

        // then
        assertThatThrownBy(() -> orderTables.validateSizeAndUngroup(invalidSize))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
