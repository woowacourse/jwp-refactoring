package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.InvalidOrderTableToGroupException;
import kitchenpos.exception.NotCompletedOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹에 포함되는 주문 테이블이 비어있지 않으면 예외가 발생한다.")
    void construct_notCompletedOrderTableIncluded() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, false);
        final OrderTable orderTable2 = new OrderTable(1, true);

        // when, then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 포함되는 주문 테이블이 다른 테이블 그룹에 이미 포함되어 있는 경우 예외가 발생한다.")
    void construct_includedGroupOrderTableIncluded() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, true);
        final OrderTable orderTable2 = new OrderTable(1, true);
        new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)))
                .isExactlyInstanceOf(InvalidOrderTableToGroupException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, true);
        final OrderTable orderTable2 = new OrderTable(1, true);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // when
        tableGroup.ungroup();

        // then
        assertThat(orderTables).extracting("tableGroupId")
                .containsExactly(null, null);
    }

    @Test
    @DisplayName("테이블이 완료되지 않은 테이블 그룹을 해제할 경우 예외가 발생한다.")
    void ungroup_notCompletedOrderTable() {
        // given
        final OrderTable orderTable1 = new OrderTable(1, true);
        final OrderTable orderTable2 = new OrderTable(1, true);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        Order.of(orderTable1, Arrays.asList(orderLineItem1, orderLineItem2));

        // when, then
        assertThatThrownBy(tableGroup::ungroup)
                .isExactlyInstanceOf(NotCompletedOrderTableException.class);
    }

}
