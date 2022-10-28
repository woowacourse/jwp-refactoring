package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderTableTest {

    private Long id = 1L;
    private Long tableGroupId = 1L;
    private int numberOfGuests = 3;
    private boolean empty = false;

    @Test
    void order_table을_생성할_수_있다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);

        Assertions.assertAll(
                () -> assertThat(orderTable.getId()).isEqualTo(id),
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(empty)
        );
    }

    @Test
    void order_table을_비울_수_있다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);
        orderTable.clear();
        Assertions.assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void order_table을_채울_수_있다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);
        orderTable.fillOrderTableGroup(3L);
        Assertions.assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(3L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"true, false", "false, true"})
    void empty_상태를_바꿀_수_있다(boolean before, boolean after) {
        OrderTable orderTable = new OrderTable(id, null, numberOfGuests, before);
        orderTable.changeEmpty(after);
        assertThat(orderTable.isEmpty()).isEqualTo(after);
    }

    @Test
    void empty_상태를_바꿀_때_table_group_id가_null이_아니면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, false);
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guest_수를_바꿀_수_있다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);
        orderTable.changeNumberOfGuests(10);
        Assertions.assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isNotEqualTo(numberOfGuests),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(10)
        );
    }

    @Test
    void guest_수를_바꿀_때_비어있으면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, true);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guest_수를_바꿀_때_음수이면_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(id, tableGroupId, numberOfGuests, empty);
        int negativeNumberOfGuests = -1;
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(negativeNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
