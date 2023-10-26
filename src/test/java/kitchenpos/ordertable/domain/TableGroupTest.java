package kitchenpos.ordertable.domain;

import kitchenpos.BaseTest;
import kitchenpos.ordertable.exception.TableGroupException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

class TableGroupTest extends BaseTest {

    @Test
    void 테이블_그룹에_주문_테이블을_추가한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);

        // when
        OrderTables orderTables = new OrderTables(List.of(firstOrderTable, secondOrderTable));
        orderTables.group(1L);
        List<Long> orderTableIds = orderTables.getOrderTables()
                .stream()
                .map(OrderTable::getTableGroupId)
                .collect(Collectors.toList());

        // then
        Assertions.assertThat(orderTableIds).containsExactlyInAnyOrderElementsOf(List.of(1L, 1L));
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블의_개수가_1개_이하이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);

        // when, then
        Assertions.assertThatThrownBy(() -> new OrderTables(List.of(firstOrderTable)))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블이_주문이_가능한_상태면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), false);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        OrderTables orderTables = new OrderTables(List.of(firstOrderTable, secondOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> orderTables.group(2L))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블이_이미_다른_테이블_그룹에_속해있으면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        firstOrderTable.changeTableGroupId(1L);
        OrderTables orderTables = new OrderTables(List.of(firstOrderTable, secondOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> orderTables.group(2L))
                .isInstanceOf(TableGroupException.class);
    }
}
