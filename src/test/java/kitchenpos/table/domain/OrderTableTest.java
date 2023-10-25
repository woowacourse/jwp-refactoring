package kitchenpos.table.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void 테이블_그룹을_변경한다() {
        var orderTable = new OrderTable(3, true);
        var tableGroup = new TableGroup();
        orderTable.changeTableGroup(tableGroup);

        assertThat(orderTable.existsTableGroup()).isTrue();
    }

    @Test
    void 테이블_그룹을_제거한다() {
        var orderTable = new OrderTable(3, true);
        var tableGroup = new TableGroup();
        orderTable.changeTableGroup(tableGroup);

        orderTable.ungroup();

        assertAll(
                () -> assertThat(orderTable.isEmpty()).isFalse(),
                () -> assertThat(orderTable.existsTableGroup()).isFalse()
        );
    }

    @Test
    void 손님_수를_변경하려는_테이블이_빈_테이블이면_예외발생() {
        var orderTable = new OrderTable(3, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경하려는_손님_수가_음수면_예외발생() {
        var orderTable = new OrderTable(2, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님_수를_변경한다() {
        var orderTable = new OrderTable(2, false);
        int expectedGuestsNum = 6;
        orderTable.changeNumberOfGuests(expectedGuestsNum);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedGuestsNum);
    }

    @Test
    void 상태를_변경하려는_테이블_속한_그룹이_있으면_예외발생() {
        var orderTable = new OrderTable(3, false);
        var tableGroup = new TableGroup();
        orderTable.changeTableGroup(tableGroup);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_상태를_변경한다() {
        var orderTable = new OrderTable(3, true);
        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 테이블이_속한_그룹이_있는지_확인한다() {
        var orderTable = new OrderTable(3, false);
        var tableGroup = new TableGroup();
        orderTable.changeTableGroup(tableGroup);

        assertThat(orderTable.existsTableGroup()).isTrue();
    }
}
