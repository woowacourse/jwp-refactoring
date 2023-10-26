package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹을 정상적으로 생성한다.")
    void createTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(null, 2, true);
        final List<OrderTable> orderTables = List.of(orderTable);

        // when
        final TableGroup tableGroup = new TableGroup(orderTables);

        // then
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).hasSize(1),
                () -> assertThat(tableGroup.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있지 않은 경우 예외가 발생한다.")
    void throwsExceptionWhenOrderTableIsNotEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(null, 2, false);
        final List<OrderTable> orderTables = List.of(orderTable);

        // when, then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있어야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 생성할 때 주문 테이블의 테이블 그룹이 비어있지 않은 경우 예외가 발생한다.")
    void throwsExceptionWhenOrderTableHasTableGroup() {
        // given
        final TableGroup tableGroup = new TableGroup(new ArrayList<>());
        final OrderTable orderTable = new OrderTable(tableGroup, 2, true);
        final List<OrderTable> orderTables = List.of(orderTable);

        // when, then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹은 비어있어야 합니다.");
    }

}
