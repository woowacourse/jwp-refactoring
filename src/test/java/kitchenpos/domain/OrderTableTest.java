package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.group.TableGroup;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("그룹을 지정한다.")
    void arrangeGroup() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(true)
                .build();
        TableGroup tableGroup = new TableGroup();

        orderTable.arrangeGroup(tableGroup);

        assertAll(() -> {
            assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
            assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    @DisplayName("그룹을 해제한다.")
    void ungroup() {
        OrderTable orderTable = OrderTable.builder()
                .tableGroup(new TableGroup())
                .numberOfGuests(2)
                .empty(true)
                .build();

        orderTable.ungroup();

        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("테이블 상태를 변경한다.")
    void changeEmpty() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(true)
                .build();

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("단체 지정된 테이블 상태 변경 시 예외가 발생한다.")
    void changeEmptyFails() {
        OrderTable orderTable = OrderTable.builder()
                .tableGroup(new TableGroup())
                .numberOfGuests(2)
                .empty(true)
                .build();

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블은 비활성화할 수 없습니다.");
    }

    @Test
    @DisplayName("손님 수를 변경한다.")
    void changeNumberOfGuest() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(false)
                .build();

        orderTable.changeNumberOfGuest(5);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("손님 수를 음수로 변경할 경우 예외가 발생한다.")
    void changeNumberOfGuestBelowZero() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(false)
                .build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 0명 미만일 수 없습니다.");
    }

    @Test
    @DisplayName("비활성화된 테이블의 손님 수를 변경할 경우 예외가 발생한다.")
    void changeNumberOfGuestEmptyTable() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(true)
                .build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비활성화된 주문 테이블의 손님 수는 변경할 수 없습니다.");
    }
}
