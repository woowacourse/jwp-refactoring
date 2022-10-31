package kitchenpos.domain;

import static kitchenpos.Fixture.DomainFixture.GUEST_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.GuestNumberChangeDisabledException;
import kitchenpos.exception.TableEmptyDisabledException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("고객의 수를 변경할 수 있다.")
    @Test
    void changeGuestNumber() {
        OrderTable orderTable = new OrderTable(GUEST_NUMBER, false);
        GuestNumber guestNumber = new GuestNumber(3);

        orderTable.changeGuestNumber(guestNumber);

        assertThat(orderTable.getGuestNumber()).isEqualTo(guestNumber.getValue());
    }

    @DisplayName("empty 상태가 아닌 테이블의 고객 수를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeGuestNumber_Exception_NotEmpty() {
        OrderTable orderTable = new OrderTable(GUEST_NUMBER, true);

        assertThatThrownBy(() -> orderTable.changeGuestNumber(new GuestNumber(10)))
                .isInstanceOf(GuestNumberChangeDisabledException.class);
    }

    @DisplayName("empty 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(GUEST_NUMBER, false);

        orderTable.setEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 속한 테이블의 empty 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeEmpty_Exception_GroupedTable() {
        OrderTable orderTable = new OrderTable(GUEST_NUMBER, false, new TableGroup());

        assertThatThrownBy(() -> orderTable.setEmpty(true))
                .isInstanceOf(TableEmptyDisabledException.class)
                .hasMessage("Table Group으로 묶인 테이블은 empty를 변경할 수 없습니다.");
    }
}
