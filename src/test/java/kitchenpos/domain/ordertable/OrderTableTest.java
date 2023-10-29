package kitchenpos.domain.ordertable;

import kitchenpos.DomainTest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableNumberOfGuests;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class OrderTableTest {
    @Test
    void 테이블의_상태를_변경한다() {
        final OrderTable orderTable = new OrderTable(null, new OrderTableNumberOfGuests(5), true);
        orderTable.updateEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 테이블의_그룹을_변경한다() {
        final OrderTable orderTable = new OrderTable(null, new OrderTableNumberOfGuests(5), true);
        final TableGroup newTableGroup = new TableGroup(LocalDateTime.now());
        orderTable.updateTableGroup(newTableGroup);

        assertThat(orderTable.getTableGroup()).isEqualTo(newTableGroup);
    }

    @Test
    void 테이블의_방문한_손님_수를_변경한다() {
        final OrderTable orderTable = new OrderTable(null, new OrderTableNumberOfGuests(5), true);
        orderTable.updateNumberOfGuests(new OrderTableNumberOfGuests(3));

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void 테이블의_상태가_비어있지_않으면_테이블_그룹을_생성할_수_없다() {
        final OrderTable orderTable = new OrderTable(null, new OrderTableNumberOfGuests(5), false);
        assertThatThrownBy(() -> orderTable.validateCreateTableGroup())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹이_비어있지_않으면_테이블_그룹을_생성할_수_없다() {
        final OrderTable orderTable = new OrderTable(new TableGroup(LocalDateTime.now()), new OrderTableNumberOfGuests(5), true);
        assertThatThrownBy(() -> orderTable.validateCreateTableGroup())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹이_비어있지_않으면_테이블_상태를_바꿀_수_없다() {
        final OrderTable orderTable = new OrderTable(new TableGroup(LocalDateTime.now()), new OrderTableNumberOfGuests(5), true);
        assertThatThrownBy(() -> orderTable.validateChangeEmpty())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_빈_테이블이면_예외를_발생시킨다() {
        final OrderTable orderTable = new OrderTable(new TableGroup(LocalDateTime.now()), new OrderTableNumberOfGuests(5), true);
        assertThatThrownBy(() -> orderTable.validateIsEmpty())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
