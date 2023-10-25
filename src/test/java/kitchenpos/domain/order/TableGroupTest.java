package kitchenpos.domain.order;

import kitchenpos.BaseTest;
import kitchenpos.order.exception.TableGroupException;
import kitchenpos.order.domain.GuestNumber;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class TableGroupTest extends BaseTest {

    @Test
    void 테이블_그룹에_주문_테이블을_추가한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when
        tableGroup.addOrderTables(List.of(firstOrderTable, secondOrderTable));

        // then
        Assertions.assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블의_개수가_1개_이하이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(firstOrderTable)))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블이_주문이_가능한_상태면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), false);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(firstOrderTable, secondOrderTable)))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블_그룹에_주문_테이블을_추가할_때_주문_테이블이_이미_다른_테이블_그룹에_속해있으면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        TableGroup anotherTableGroup = new TableGroup(LocalDateTime.now());
        anotherTableGroup.addOrderTables(List.of(firstOrderTable, secondOrderTable));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(firstOrderTable, secondOrderTable)))
                .isInstanceOf(TableGroupException.class);
    }
}
