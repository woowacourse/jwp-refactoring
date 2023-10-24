package kitchenpos.tablegroup.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static kitchenpos.common.fixtures.TableGroupFixtures.TABLE_GROUP1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.exception.TableGroupException;
import kitchenpos.tablegroup.exception.TableGroupException.CannotCreateTableGroupStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블 사이즈와 저장된 주문 테이블 사이즈가 다르면 예외가 발생한다.")
    void throws_notSameOrderTableSize() {
        // given
        final List<OrderTable> orderTables = List.of(ORDER_TABLE1(), ORDER_TABLE1());
        final int orderTableSize = 1;
        final int foundOrderTableSize = 2;

        // when & then
        assertThatThrownBy(() -> TableGroup.create(orderTables, orderTableSize, foundOrderTableSize))
                .isInstanceOf(TableGroupException.NotFoundOrderTableExistException.class)
                .hasMessage("[ERROR] 주문 테이블 목록 중 존재하지 않는 주문 테이블이 있습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않은 상태이면 예외가 발생한다.")
    void throws_orderTableIsEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
        final List<OrderTable> orderTables = List.of(orderTable);
        final int orderTableSize = 1;

        // when & then
        assertThatThrownBy(() -> TableGroup.create(orderTables, orderTableSize, orderTableSize))
                .isInstanceOf(CannotCreateTableGroupStateException.class)
                .hasMessage("[ERROR] 주문 테이블이 빈 상태가 아니거나 테이블 그룹이 존재하지 않을 때 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("찾은 주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    void throws_AlreadyExistTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
        orderTable.updateTableGroupId(TABLE_GROUP1().getId());
        final List<OrderTable> orderTables = List.of(orderTable);
        final int orderTableSize = 1;

        // when & then
        assertThatThrownBy(() -> TableGroup.create(orderTables, orderTableSize, orderTableSize))
                .isInstanceOf(CannotCreateTableGroupStateException.class)
                .hasMessage("[ERROR] 주문 테이블이 빈 상태가 아니거나 테이블 그룹이 존재하지 않을 때 테이블 그룹을 생성할 수 없습니다.");
    }

}
