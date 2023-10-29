package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.OrderTable;
import kitchenpos.tablegroup.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 단체지정을_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    void 빈_테이블로_설정_할때_단체_지정이_되어있는_경우_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, false);

        // when, then
        Assertions.assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Nested
    class 방문한_손님_수_변경 {

        @Test
        void 방문한_손님_수가_음수인_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable(1L, 0, false);

            // when, then
            Assertions.assertThatThrownBy(() -> orderTable.setNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블인_경우_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable(1L, 0, true);

            // when, then
            Assertions.assertThatThrownBy(() -> orderTable.setNumberOfGuests(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
