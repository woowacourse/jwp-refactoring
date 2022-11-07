package kitchenpos.common.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void addTableGroupId_메서드는_테이블_그룹을_등록하고_비어있지_않은_테이블로_수정한다() {
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        long tableGroupId = 10L;
        orderTable.addTableGroupId(tableGroupId);

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void removeTableGroupId_메서드는_테이블_그룹을_제거하고_비어있지_않은_테이블로_수정한다() {
        OrderTable orderTable = new OrderTable(1L, 10L, 0, true);
        orderTable.removeTableGroupId();

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("changeNumberOfGuests 메서드는")
    @Nested
    class ChangeNumberOfGuestsTest {

        @Test
        void 테이블의_고객_수를_음수로_수정하려는_경우_예외를_발생시킨다() {
            OrderTable emptyTable = new OrderTable(1L, null, 5, false);
            assertThatThrownBy(() -> emptyTable.changeNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블인_경우_예외를_발생시킨다() {
            OrderTable emptyTable = new OrderTable(0, true);
            assertThatThrownBy(() -> emptyTable.changeNumberOfGuests(10))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void changeEmpty_메서드는_단체로_지정된_테이블에서_호출하는_경우_예외를_발생시킨다() {
        OrderTable groupedTable = new OrderTable(1L, 10L, 5, false);
        assertThatThrownBy(() -> groupedTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
