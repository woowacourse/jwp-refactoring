package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 그룹에 속해있으면 상태를 변경할 수 없다.")
    void changeEmptyTest() {

        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(1L, tableGroup, null, 1, true);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeEmpty(false);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }

    @Test
    @DisplayName("손님의 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsTest() {

        // given
        OrderTable orderTable = new OrderTable(1L, null, null, 1, false);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeNumberOfGuests(-1);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    void changeNumberOfGuestsFailTest() {

        // given
        OrderTable orderTable = new OrderTable(1L, null, null, 1, true);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeNumberOfGuests(-1);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }
}
