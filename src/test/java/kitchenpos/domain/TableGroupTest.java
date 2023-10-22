package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    void 단체_지정은_주문_테이블이_2개보다_작으면_예외가_발생한다() {
        // given
        List<OrderTable> orderTables = List.of(orderTable(10, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        // expect
        assertThatThrownBy(() -> tableGroup.changeOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이여야 합니다");
    }

    @Test
    void 단체_지정은_빈_테이블이_아니면_예외가_발생한다() {
        // given
        List<OrderTable> orderTables = List.of(orderTable(10, true), orderTable(5, true));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        List<OrderTable> changeOrderTable = List.of(orderTable(1, false), orderTable(10, true));

        // expect
        assertThatThrownBy(() -> tableGroup.changeOrderTables(changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정은 빈 테이블만 가능합니다");
    }
}
