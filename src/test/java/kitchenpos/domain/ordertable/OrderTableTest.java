package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 손님의_명_수가_0명보다_적으면_예외가_발생한다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(
                () -> new OrderTable(tableGroup.getId(), -1, false)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_수를_변경할_때_주문_테이블이_비어_있으면_예외가_발생한다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(
                () -> {
                    OrderTable orderTable = new OrderTable(tableGroup.getId(), 2, true);
                    orderTable.validateEmptyForChangeGuestNumber();
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태로_변경할_때_테이블_그룹이_비어있지_않으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> {
                    OrderTable orderTable = new OrderTable(1L, 2, false);
                    orderTable.validateTableGroupForChangeEmpty();
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
